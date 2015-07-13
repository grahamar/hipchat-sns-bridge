package controllers

import javax.inject.Inject

import io.evanwong.oss.hipchat.v2.HipChatClient
import io.evanwong.oss.hipchat.v2.rooms.{MessageFormat, MessageColor}
import play.api.libs.concurrent.Execution.Implicits._
import play.api.{Configuration, Logger, Application}
import play.api.libs.json.{Json, JsValue}
import play.api.libs.ws.WS
import play.api.mvc._

import scala.concurrent.Future

class HipchatBridge @Inject() (hipchat: HipChatClient, app: Application, configuration: Configuration) extends Controller {

  def roomNotification(roomId: Int, apiToken: String) = Action.async(parse.tolerantJson) { implicit request =>
    Logger.info(
      s"""
        |> ${request.method} ${request.uri}
        |${request.headers.headers.map(kv => s"> ${kv._1}: ${kv._2}").mkString("\n")}
        |
        |${Json.prettyPrint(request.body)}
      """.stripMargin)
    val messageType = (request.body \ "Type").as[String]
    messageType match {
      case "SubscriptionConfirmation" => confirmSnsTopicSubscription(request)
      case "Notification" => notifyHipchatRoom(roomId, apiToken, request)
    }
  }

  private def notifyHipchatRoom(roomId: Int, apiToken: String, request: Request[JsValue]) = Future {
    val (message, colour) = buildMessage(request)
    val notificationBuilder = hipchat.prepareSendRoomNotificationRequestBuilder(roomId.toString, message, apiToken)
    notificationBuilder.setColor(colour)
    notificationBuilder.setMessageFormat(MessageFormat.TEXT)
    notificationBuilder.setNotify(false)
    notificationBuilder.build().execute()
    Ok("")
  }

  private def confirmSnsTopicSubscription(request: Request[JsValue]) = {
    val subscribeUrl = (request.body \ "SubscribeURL").as[String]
    WS.url(subscribeUrl)(app).get().map(_ => Ok(""))
  }

  private def buildMessage(request: Request[JsValue]): (String, MessageColor) = {
    //AutoScaling Messages have a 'cause' section in the message json
    val cause = (request.body \ "Cause").asOpt[String]
    // Pretty Alarm messages received from SNS
    val alarmName = (request.body \ "AlarmName").asOpt[String]

    if(cause.isDefined) {
      val autoScalingGroupName = (request.body \ "AutoScalingGroupName").as[String]
      val description = (request.body \ "Description").as[String]
      s"$autoScalingGroupName - $description - ${cause.get.substring(0, cause.get.indexOf('.'))}" -> MessageColor.PURPLE
    } else if (alarmName.isDefined) {
      val newStateValue = (request.body \ "NewStateValue").as[String]
      val newStateReason = (request.body \ "NewStateReason").as[String]
      val stateChangeTime = (request.body \ "StateChangeTime").as[String]
      s"$newStateValue - ${alarmName.get} - $newStateReason - $stateChangeTime" -> MessageColor.PURPLE
    } else {
      val event = (request.body \ "Event").asOpt[String]
      val colour = if(event.isDefined && event.exists(_.contains("EC2_INSTANCE_LAUNCH"))) {
        MessageColor.GREEN
      } else if (event.isDefined && event.exists(_.contains("EC2_INSTANCE_TERMINATE"))) {
        MessageColor.RED
      } else MessageColor.PURPLE

      (request.body \ "Message").as[String] -> colour
    }
  }

}
