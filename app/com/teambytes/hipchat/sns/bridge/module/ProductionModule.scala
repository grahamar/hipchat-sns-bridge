package com.teambytes.hipchat.sns.bridge.module

import com.google.inject.AbstractModule
import com.imadethatcow.hipchat.rooms.RoomNotifier
import net.codingwell.scalaguice.ScalaModule
import play.api.libs.ws.WSClient
import play.api.{Configuration, Environment}
import play.api.libs.ws.ahc.AhcWSClient

class ProductionModule(environment: Environment, configuration: Configuration) extends AbstractModule with ScalaModule {
  def configure(): Unit = {
    bind[RoomNotifier].toInstance(new RoomNotifier(configuration.getString("com.imadethatcow.hipchat.auth_token").get))
    bind[WSClient].toInstance(AhcWSClient())
  }
}
