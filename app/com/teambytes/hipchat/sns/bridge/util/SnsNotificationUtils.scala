package com.teambytes.hipchat.sns.bridge.util

import com.teambytes.hipchat.sns.bridge.data.models.DeploymentNotification
import play.api.Logger
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}

import scala.util.Try
import scala.util.control.NonFatal

object SnsNotificationUtils {
  def readNotificationMessage(message: String): String = {
    Logger.info(s"Attempting JSON parsing of message: '$message'")
    Try(Json.parse(message)).recover {
      case NonFatal(e) =>
        Logger.info("Failed to parse JSON in message.", e)
        throw e
    }.map(handleJson).getOrElse(message)
  }

  private def handleJson(msgJson: JsValue): String = {
    msgJson.validate[DeploymentNotification] match {
      case JsSuccess(deployNotification, _) => handleDeployNotification(deployNotification)
      case JsError(e) =>
        Logger.info(s"Failed to parse DeploymentNotification JSON in message [${JsError.toJson(e).toString}].")
        s"""<pre>${Json.prettyPrint(msgJson)}</pre>"""
    }
  }

  private def handleDeployNotification(deployNotification: DeploymentNotification): String = {
    deployNotification.status match {
      case "CREATED" =>
        s"""
           |Deployment: ${deployNotification.deploymentId}
           |Deploying application <b>'${deployNotification.applicationName}'</b> group <b>'${deployNotification.deploymentGroupName}'</b>
        """.stripMargin

      case "SUCCEEDED" =>
        s"""
           |Deployment: ${deployNotification.deploymentId}
           |Finished application <b>'${deployNotification.applicationName}'</b> group <b>'${deployNotification.deploymentGroupName}'</b> deployment.
           |${deployNotification.deploymentOverview.map(Json.parse).map(js => s"Result: ${Json.prettyPrint(js)}").getOrElse("")}
        """.stripMargin

      case otherStatus =>
        s"""
           |Deployment: ${deployNotification.deploymentId}
           |Application <b>'${deployNotification.applicationName}'</b> group <b>'${deployNotification.deploymentGroupName}'</b> deployment.
           |Status: $otherStatus
        """.stripMargin
    }

  }

}