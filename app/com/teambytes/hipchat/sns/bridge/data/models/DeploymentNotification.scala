package com.teambytes.hipchat.sns.bridge.data.models

import play.api.libs.json.Json

case class DeploymentNotification(region: String,
                                  accountId: String,
                                  eventTriggerName: String,
                                  applicationName: String,
                                  deploymentId: String,
                                  deploymentGroupName: String,
                                  deploymentOverview: Option[String],
                                  createTime: String,
                                  completeTime: Option[String],
                                  status: String)

object DeploymentNotification {
  implicit val format = Json.format[DeploymentNotification]
}