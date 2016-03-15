package com.teambytes.hipchat.sns.bridge.data

import com.teambytes.hipchat.sns.bridge.data.models.{Token, Tenant}
import com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallData

import scala.concurrent.{Future, ExecutionContext}

trait TenantStore {
  def install(installData: HipchatInstallData)(implicit ec: ExecutionContext): Future[Unit]
  def uninstall(oauthId: String)(implicit ec: ExecutionContext): Future[Unit]
  def listTenants()(implicit ec: ExecutionContext): Future[Set[Tenant]]
  def getToken(tenant: Tenant)(implicit ec: ExecutionContext): Future[Token]
}
