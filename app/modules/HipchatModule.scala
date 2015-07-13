package modules

import com.amazonaws.services.autoscaling.{AmazonAutoScaling, AmazonAutoScalingClient}
import io.evanwong.oss.hipchat.v2.HipChatClient
import lib.{NewRelic, NoOpNewRelic, WsNewRelic}
import play.api.inject.Module
import play.api.{Configuration, Environment}

class HipchatModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = {
    val newRelicEnabled = configuration.getBoolean("newrelic.enabled").getOrElse(false)
    val newRelicClient = if(newRelicEnabled) { classOf[WsNewRelic] } else { classOf[NoOpNewRelic] }
    Seq(
      bind(classOf[HipChatClient]).toInstance(new HipChatClient()),
      bind(classOf[AmazonAutoScaling]).toInstance(new AmazonAutoScalingClient()),
      bind(classOf[NewRelic]).to(newRelicClient)
    )
  }
}
