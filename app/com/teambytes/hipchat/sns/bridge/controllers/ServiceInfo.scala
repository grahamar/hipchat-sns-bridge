package com.teambytes.hipchat.sns.bridge.controllers

import javax.inject.Singleton

import buildinfo.BuildInfo
import com.teambytes.hipchat.sns.bridge.v0.models.json._
import org.joda.time.DateTime
import play.api.libs.json._
import play.api.mvc.{Action, Controller}

@Singleton
class ServiceInfo extends Controller {

  def get() = Action {
    Ok(Json.toJson(com.teambytes.hipchat.sns.bridge.v0.models.ServiceInfo(
      BuildInfo.name,
      BuildInfo.version,
      BuildInfo.scalaVersion,
      BuildInfo.sbtVersion,
      new DateTime(BuildInfo.builtAtMillis)
    )))
  }

}
