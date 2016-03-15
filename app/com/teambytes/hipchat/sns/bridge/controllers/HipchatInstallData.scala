package com.teambytes.hipchat.sns.bridge.controllers

import javax.inject.{Inject, Singleton}

import com.teambytes.hipchat.sns.bridge.data.TenantStore
import com.teambytes.hipchat.sns.bridge.v0.models
import com.teambytes.hipchat.sns.bridge.v0.models.Error
import com.teambytes.hipchat.sns.bridge.v0.models.json._
import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsError, Json}
import play.api.libs.ws.WSClient
import play.api.mvc.{Action, Controller}

import scala.concurrent.Future

@Singleton
class HipchatInstallData @Inject() (store: TenantStore, ws: WSClient) extends Controller {

  def post() = Action.async(parse.json) { implicit request =>
    request.body.validate[models.HipchatInstallData].fold(
      errors => Future.successful(BadRequest(Json.toJson(Error(JsError.toJson(errors).toString)))),
      installData => store.install(installData).map(_ => Ok(Json.toJson("Successfully installed")))
    )
  }

  def get(installable_url: String, redirect_url: String) = Action.async { implicit request =>
    Logger.info(s"Calling Hipchat for install data... [$installable_url]")
    ws.url(installable_url).get().flatMap { response =>
      if(response.status == 200) {
        store.install(Json.fromJson[models.HipchatInstallData](response.json).get)
          .map(_ => TemporaryRedirect(redirect_url))
      } else {
        Future.successful(BadGateway(Json.toJson(response.statusText)))
      }
    }
  }

  def deleteByOauthId(oauthId: String) = Action.async { implicit request =>
    store.uninstall(oauthId).map(_ => Ok(Json.toJson("Successfully uninstalled")))
  }

}
