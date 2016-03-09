package com.teambytes.hipchat.sns.bridge.controllers

import javax.inject.{Inject, Singleton}

import com.teambytes.hipchat.sns.bridge.data.TenantStore
import com.teambytes.hipchat.sns.bridge.v0.models
import com.teambytes.hipchat.sns.bridge.v0.models.Error
import com.teambytes.hipchat.sns.bridge.v0.models.json._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

@Singleton
class HipchatInstallData @Inject() (store: TenantStore) extends Controller {

  def post() = Action.async(parse.json) { implicit request =>
    request.body.validate[models.HipchatInstallData].fold(
      errors => Future.successful(BadRequest(Json.toJson(Error(JsError.toJson(errors).toString)))),
      installData => store.install(installData).map(_ => Ok(Json.toJson("Successfully installed")))
    )
  }

  def deleteByOauthId(oauthId: String) = Action.async { implicit request =>
    store.uninstall(oauthId).map(_ => Ok(Json.toJson("Successfully uninstalled")))
  }

}
