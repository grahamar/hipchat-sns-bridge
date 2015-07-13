package modules

import io.evanwong.oss.hipchat.v2.HipChatClient
import play.api.inject.Module
import play.api.{Configuration, Environment}
import play.api.libs.concurrent.Execution.Implicits._

class HipchatModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = {
    Seq(bind(classOf[HipChatClient]).toInstance(new HipChatClient()))
  }
}
