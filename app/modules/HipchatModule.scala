package modules

import com.amazonaws.services.autoscaling.{AmazonAutoScaling, AmazonAutoScalingClient}
import io.evanwong.oss.hipchat.v2.HipChatClient
import play.api.inject.Module
import play.api.{Configuration, Environment}

class HipchatModule extends Module {
  def bindings(environment: Environment, configuration: Configuration) = Seq(
    bind(classOf[HipChatClient]).toInstance(new HipChatClient()),
    bind(classOf[AmazonAutoScaling]).toInstance(new AmazonAutoScalingClient())
  )
}
