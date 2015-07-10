package modules

import com.zaneli.escalade.hipchat.Rooms
import play.api.inject.Module
import play.api.{Configuration, Environment}

class HipchatModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = {
    val hipchatApiToken = configuration.getString("hipchat.api.token").
      getOrElse(throw new IllegalStateException("You must provide a hipchat API token."))
    val rooms = new com.zaneli.escalade.hipchat.Rooms(hipchatApiToken)
    Seq(bind(classOf[Rooms]).toInstance(rooms))
  }
}
