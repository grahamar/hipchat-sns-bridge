package com.teambytes.hipchat.sns.bridge.data.models

import com.teambytes.hipchat.sns.bridge.v0.models.{HipchatCapabilitiesDescriptor, HipchatInstallData}

object Tenant {
  def apply(d: HipchatInstallData, capabilities: HipchatCapabilitiesDescriptor): Tenant = {
    Tenant(d.oauthId, d.oauthSecret, capabilities.links.api.get, capabilities.capabilities.flatMap(_.oauth2Provider).flatMap(_.tokenUrl).get)
  }
}

case class Tenant(id: String, secret: String, linkApi: String, linkToken: String)
