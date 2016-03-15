package com.teambytes.hipchat.sns.bridge.module

import com.google.inject.AbstractModule
import com.teambytes.hipchat.sns.bridge.data.{CachingDBTenantStore, TenantStore}
import net.codingwell.scalaguice.ScalaModule
import play.api.{Configuration, Environment}

class ProductionModule(environment: Environment, configuration: Configuration) extends AbstractModule with ScalaModule {
  def configure(): Unit = {
    bind[TenantStore].to[CachingDBTenantStore].asEagerSingleton
  }
}
