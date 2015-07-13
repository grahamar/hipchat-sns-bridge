package lib

import javax.inject.Inject

import play.api.{Logger, Application, Configuration}
import play.api.libs.ws.WS

import scala.concurrent.{ExecutionContext, Future}

object NewRelic {
  val NewEnvironmentLaunchedRegex = """(?s).*Message: Successfully launched environment.*\n\nEnvironment: (.*?)\nApplication: (.*?)\n.*""".r
}

trait NewRelic {
  def recordDeployment(appName: String, environmentName: Option[String])(implicit ec: ExecutionContext): Future[Unit]
}

class NoOpNewRelic extends NewRelic {
  def recordDeployment(appName: String, environmentName: Option[String])(implicit ec: ExecutionContext): Future[Unit] = Future.successful(())
}

class WsNewRelic @Inject() (configuration: Configuration, app: Application) extends NewRelic {

  private val apiToken: Option[String] = configuration.getString("newrelic.api.token")

  def recordDeployment(appName: String, environmentName: Option[String])(implicit ec: ExecutionContext): Future[Unit] = {
    apiToken.map { token =>
      val parameters = Map("deployment[app_name]" -> Seq(appName)) ++
        environmentName.map(en => Map("deployment[description]" -> Seq(en))).getOrElse(Map.empty)
      WS.url("https://api.newrelic.com/deployments.xml")(app).withHeaders("x-api-key" -> token).post(parameters).map(_ => ())
    }.getOrElse {
      Logger.info("Not recording NewRelic deployment as no API token has been provided.")
      Future.successful(())
    }
  }
}
