package modules

import com.imadethatcow.hipchat.rooms.RoomNotifier
import play.api.inject.Module
import play.api.{Configuration, Environment}
import lib.Contexts.notifyerExecutionContext

class HipchatModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = {
    val hipchatApiToken = configuration.getString("hipchat.api.token").
      getOrElse(throw new IllegalStateException("You must provide a hipchat API token."))
    Seq(bind(classOf[RoomNotifier]).toInstance(new RoomNotifier(hipchatApiToken)))
  }
}
