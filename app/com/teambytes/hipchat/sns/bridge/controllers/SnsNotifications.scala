package com.teambytes.hipchat.sns.bridge.controllers

import javax.inject.{Inject, Singleton}

import com.imadethatcow.hipchat.common.enums.{MessageFormat, Color}
import com.imadethatcow.hipchat.rooms.RoomNotifier
import com.teambytes.hipchat.sns.bridge.util.SnsSignatureUtils
import com.teambytes.hipchat.sns.bridge.v0.models.{Error, SnsNotification}
import play.api.cache.Cached
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, Json}
import play.api.libs.ws.WSClient
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class SnsNotifications @Inject()(notifier: RoomNotifier, ws: WSClient, cached: Cached) extends Controller {

  /**
    * Cache 200 OK responses for 'roomId.messageId' as SNS will retry. From the docs:
    *
    * "Make sure that your code can handle message delivery retries from Amazon SNS.
    * If Amazon SNS doesn't receive a successful response from your endpoint, it attempts to deliver the message again.
    * This applies to all messages, including the subscription confirmation message.
    * By default, if the initial delivery of the message fails,
    * Amazon SNS attempts up to three retries with a delay between failed attempts set at 20 seconds.
    * Note that the message request times out at 15 seconds.
    * This means that if the message delivery failure was caused by a timeout,
    * Amazon SNS will retry approximately 35 seconds after the previous delivery attempt.
    * If you don't like the default delivery policy, you can set a different delivery policy on the endpoint.
    * To be clear, Amazon SNS attempts to retry only after a delivery attempt has failed.
    * You can identify a message using the x-amz-sns-message-id header field.
    * By comparing the IDs of the messages you have processed with incoming messages,
    * you can determine whether the message is a retry attempt."
    */
  def post(roomId: String) = cached.status((rh: RequestHeader) => s"$roomId.${rh.headers("x-amz-sns-message-id")}", 200, 150) {
    Action.async(parse.tolerantJson) { implicit request =>
      request.body.validate[SnsNotification].fold(
        errors => Future.successful(BadRequest(Json.toJson(Error(JsError.toJson(errors).toString)))),
        snsNotification => handleSnsMessage(roomId, snsNotification)
      )
    }
  }

  private def handleSnsMessage[T](roomId: String, snsMessage: SnsNotification)(implicit request: Request[T]): Future[Result] = {
    // The signature is based on SignatureVersion 1. If the sig version is something other than 1, throw an exception.
    if (snsMessage.SignatureVersion == "1") {
      if (SnsSignatureUtils.isValidSnsMessage(snsMessage)) {
        snsMessage.Type match {
          case "Notification" => handleSnsNotification(roomId, snsMessage)
          case "SubscriptionConfirmation" => handleSnsSubscription(roomId, snsMessage)
          case "UnsubscribeConfirmation" => handleSnsUnsubscribe(roomId, snsMessage)
          case other => Future.successful(BadRequest(Json.toJson(Error(s"Unsupported SNS message type"))))
        }
      } else {
        Future.successful(BadRequest(Json.toJson(Error(s"Invalid Signature"))))
      }
    } else {
      Future.successful(BadRequest(Json.toJson(Error(s"Invalid Signature Version '${snsMessage.SignatureVersion}'"))))
    }
  }

  private def handleSnsSubscription[T](roomId: String, snsNotification: SnsNotification)(implicit request: Request[T]): Future[Result] = {
    snsNotification.SubscribeURL.map { subscribeURL =>
      ws.url(subscribeURL).get().map(_ => Ok(Json.toJson("Subscribed")))
    }.getOrElse(Future.successful(BadRequest(Json.toJson("No subscription URL found."))))
  }

  private def handleSnsNotification[T](roomId: String, snsNotification: SnsNotification)(implicit request: Request[T]): Future[Result] = {
    val msg =
      s"""<p>
         |${snsNotification.Subject.map(sbj => s"""<b>$sbj</b><br/>""")}
         |<i>${snsNotification.TopicArn}</i><br/>
         |${snsNotification.Message}
         |</p>
       """.stripMargin

    notifier.sendNotification(
      roomIdOrName = roomId,
      message = snsNotification.Message,
      color = Color.purple,
      notify = false,
      messageFormat = MessageFormat.html
    ).map {
      case true => Ok(Json.toJson("Successfully posted hipchat message."))
      case false => BadGateway(Json.toJson("Failed to send hipchat message."))
    }
  }

  private def handleSnsUnsubscribe[T](roomId: String, snsNotification: SnsNotification)(implicit request: Request[T]): Future[Result] = {
    Future.successful(Ok(Json.toJson(""))) // Do nothing
  }

}
