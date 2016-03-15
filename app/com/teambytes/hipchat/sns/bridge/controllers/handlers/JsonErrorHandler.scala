package com.teambytes.hipchat.sns.bridge.controllers.handlers

import javax.inject._

import play.api._
import play.api.http.{DefaultHttpErrorHandler, MimeTypes}
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router

import scala.concurrent._

@Singleton
class JsonErrorHandler @Inject() (env: Environment,
                                  config: Configuration,
                                  sourceMapper: OptionalSourceMapper,
                                  router: Provider[Router]) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  private val JsonContent = MimeTypes.JSON

  /**
    * ====================================  Client Errors - 4xx  ====================================
    */

  // called when a route is found, but it was not possible to bind the request parameters
  override def onBadRequest(request: RequestHeader, error: String): Future[Result] = {
    Logger("requests").warn(s"Got a bad request [${request.method} - ${request.rawQueryString}] - $error")
    Future.successful(BadRequest(Json.toJson(error)).as(JsonContent))
  }

  override def onNotFound(request: RequestHeader, error: String): Future[Result] = {
    Future.successful(NotFound(Json.toJson("Not found")).as(JsonContent))
  }

  /**
    * ====================================  Server Errors - 5xx  ====================================
    */

  override def onDevServerError(request: RequestHeader, exception: UsefulException): Future[Result] = on5xxError(request, exception)

  override def onProdServerError(request: RequestHeader, exception: UsefulException): Future[Result] = on5xxError(request, exception)

  override def logServerError(request: RequestHeader, exception: UsefulException): Unit = {
    Logger("requests").error(s"Exception processing request [${request.method} ${request.uri}]", exception)
  }

  private def on5xxError(request: RequestHeader, exception: UsefulException): Future[Result] = {
    Option(exception.getCause) match {
      case Some(cause: TimeoutException) => Future.successful(GatewayTimeout(Option(cause.getMessage).getOrElse(exception.getMessage)).as(JsonContent))
      case Some(cause) => Future.successful(InternalServerError(cause.getMessage).as(JsonContent))
      case None => Future.successful(InternalServerError(exception.getMessage).as(JsonContent))
    }
  }

}
