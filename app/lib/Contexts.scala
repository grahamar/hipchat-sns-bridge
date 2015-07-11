package lib

import play.api.libs.concurrent.Akka
import play.api.Play.current
import scala.concurrent.ExecutionContext

object Contexts {
  implicit val notifyerExecutionContext: ExecutionContext = Akka.system.dispatchers.lookup("notifyer")
}
