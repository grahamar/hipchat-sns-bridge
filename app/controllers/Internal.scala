package controllers

import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}

class Internal extends Controller {
  def version = Action {
    Ok(Json.toJson(System.getProperty("service.version")))
  }
}
