package com.teambytes.hipchat.sns.bridge.controllers

import javax.inject.{Inject, Singleton}

import com.teambytes.hipchat.sns.bridge.v0.models.json._
import com.teambytes.hipchat.sns.bridge.v0.models._
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

@Singleton
class HipchatCapabilitiesDescriptors @Inject() (configuration: Configuration) extends Controller {
  def get() = Action { implicit request =>
    Ok(Json.toJson(HipchatCapabilitiesDescriptor(
      name = "hipchat-sns-bridge",
      description = "Hipchat SNS Bridge",
      key = "com.teambytes.hipchat.sns.bridge",
      links = HipchatLinks(
        self = configuration.getString("hipchat.links.self").get
      ),
      capabilities = Some(HipchatCapabilities(
        hipchatApiConsumer = Some(HipchatApiConsumer(
          scopes = Seq(HipchatScope.SendNotification, HipchatScope.SendMessage),
          fromName = configuration.getString("hipchat.capabilities.hipchatApiConsumer.fromName")
        )),
        installable = Some(HipchatInstallable(
          callbackUrl = configuration.getString("hipchat.capabilities.installable.callbackUrl"),
          installedUrl = configuration.getString("hipchat.capabilities.installable.installedUrl"),
          allowGlobal = configuration.getBoolean("hipchat.capabilities.installable.allowGlobal"),
          allowRoom = configuration.getBoolean("hipchat.capabilities.installable.allowRoom")
        ))
      ))
    )))
  }
}
