package com.teambytes.hipchat.sns.bridge.controllers

import javax.inject.{Inject, Singleton}

import com.teambytes.hipchat.sns.bridge.v0.models.json._
import com.teambytes.hipchat.sns.bridge.v0.models._
import org.apache.commons.codec.binary.{StringUtils, Base64}
import play.api.Configuration
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

@Singleton
class HipchatCapabilitiesDescriptors @Inject() (configuration: Configuration) extends Controller {

  private val CapabilitiesDescriptor = HipchatCapabilitiesDescriptor(
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
  )

  def get(redirect_to_hipchat_install: Boolean) = Action { implicit request =>
    if(redirect_to_hipchat_install) {
      val encodedDescriptor = StringUtils.newStringUtf8(Base64.encodeBase64(Json.toJson(CapabilitiesDescriptor).toString.getBytes))
      TemporaryRedirect(s"https://www.hipchat.com/addons/install?url=data:application/json;base64,$encodedDescriptor")
    } else {
      Ok(Json.toJson(CapabilitiesDescriptor))
    }
  }
}
