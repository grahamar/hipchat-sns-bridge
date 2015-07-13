package controllers

import javax.inject.Inject

import com.amazonaws.services.autoscaling.AmazonAutoScaling
import com.amazonaws.services.autoscaling.model.DescribeAutoScalingGroupsRequest
import io.evanwong.oss.hipchat.v2.HipChatClient
import io.evanwong.oss.hipchat.v2.rooms.{MessageColor, MessageFormat}
import lib.NewRelic
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.{JsValue, Json}
import play.api.libs.ws.WS
import play.api.mvc._
import play.api.{Application, Configuration, Logger}

import scala.collection.JavaConverters._
import scala.concurrent.Future
import scala.util.Try

class HipchatBridge @Inject() (hipchat: HipChatClient,
                               newRelic: NewRelic,
                               awsClient: AmazonAutoScaling,
                               configuration: Configuration,
                               app: Application) extends Controller {

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

  private def notifyHipchatRoom(roomId: Int, apiToken: String, request: Request[JsValue]) = {
    val (message, colour, format) = Try(Json.parse((request.body \ "Message").as[String])).toOption match {
      case Some(msgJson) => buildMessage(msgJson)
      case None => ((request.body \ "Message").as[String], MessageColor.PURPLE, MessageFormat.HTML)
    }

    val deploymentNotification = message match {
      case NewRelic.NewEnvironmentLaunchedRegex(envName, appName) =>
        newRelic.recordDeployment(appName, Some(envName))
      case _ => Future.successful(())
    }

    deploymentNotification.map { _ =>
      val notificationBuilder = hipchat.prepareSendRoomNotificationRequestBuilder(roomId.toString, message, apiToken)
      notificationBuilder.setColor(colour)
      notificationBuilder.setMessageFormat(MessageFormat.TEXT)
      notificationBuilder.setNotify(false)
      notificationBuilder.build().execute()
      Ok("")
    }
  }

  private def confirmSnsTopicSubscription(request: Request[JsValue]) = {
    val subscribeUrl = (request.body \ "SubscribeURL").as[String]
    WS.url(subscribeUrl)(app).get().map(_ => Ok(""))
  }

  private def buildMessage(msgJson: JsValue): (String, MessageColor, MessageFormat) = {
    //AutoScaling Messages have a 'cause' section in the message json
    val cause = (msgJson \ "Cause").asOpt[String]
    // Pretty Alarm messages received from SNS
    val alarmName = (msgJson \ "AlarmName").asOpt[String]

    if(cause.isDefined) {
      val autoScalingGroupName = (msgJson \ "AutoScalingGroupName").as[String]
      val description = (msgJson \ "Description").as[String]
      // Elastic beanstalk deployed apps have generated names, but are tagged with a human-readable 'Name', try use that.
      val asGroupDescription = awsClient.describeAutoScalingGroups(new DescribeAutoScalingGroupsRequest().withAutoScalingGroupNames(autoScalingGroupName))
      val asGroupNameTag = asGroupDescription.getAutoScalingGroups.asScala.head.getTags.asScala.find(_.getKey == "Name")
      val serviceName = asGroupNameTag.map(_.getValue).getOrElse(autoScalingGroupName)
      (s"$serviceName - $description - ${cause.get.substring(0, cause.get.indexOf('.'))}", MessageColor.PURPLE, MessageFormat.HTML)
    } else if (alarmName.isDefined) {
      val newStateValue = (msgJson \ "NewStateValue").as[String]
      val newStateReason = (msgJson \ "NewStateReason").as[String]
      val stateChangeTime = (msgJson \ "StateChangeTime").as[String]
      (s"$newStateValue - ${alarmName.get} - $newStateReason - $stateChangeTime", MessageColor.PURPLE, MessageFormat.HTML)
    } else {
      val event = (msgJson \ "Event").asOpt[String]
      val colour = if(event.isDefined && event.exists(_.contains("EC2_INSTANCE_LAUNCH"))) {
        MessageColor.GREEN
      } else if (event.isDefined && event.exists(_.contains("EC2_INSTANCE_TERMINATE"))) {
        MessageColor.RED
      } else MessageColor.PURPLE

      (Json.stringify(msgJson), colour, MessageFormat.HTML)
    }
  }

}
