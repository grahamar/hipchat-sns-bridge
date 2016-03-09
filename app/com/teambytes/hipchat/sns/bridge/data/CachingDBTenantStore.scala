package com.teambytes.hipchat.sns.bridge.data

import javax.inject.{Inject, Singleton}

import anorm._
import com.teambytes.hipchat.sns.bridge.data.models.{Token, Tenant}
import com.teambytes.hipchat.sns.bridge.v0.models.{HipchatScope, HipchatCapabilitiesDescriptor, HipchatInstallData}
import play.api.cache.CacheApi
import play.api.db.Database
import play.api.http.{MimeTypes, HeaderNames}
import play.api.libs.json.Json
import play.api.libs.ws.{WSAuthScheme, WSClient}

import scala.concurrent.duration._
import scala.concurrent.{Future, ExecutionContext}
import scala.util.control.NonFatal

@Singleton
class CachingDBTenantStore @Inject() (db: Database, ws: WSClient, cache: CacheApi) extends TenantStore {
  private val Scopes = Seq(HipchatScope.SendNotification, HipchatScope.SendMessage)
  private val TokenCacheKey = s"token.${Scopes.mkString("|")}"

  def install(installData: HipchatInstallData)(implicit ec: ExecutionContext): Future[Unit] = {
    for {
      tenant <- getTenant(installData)
      token <- getToken(tenant)
    } yield {
      db.withConnection { implicit c =>
        SQL"INSERT INTO tenant(id, secret, link_api, link_token) VALUES(${tenant.id}, ${tenant.secret}, ${tenant.linkApi}, ${tenant.linkToken})".executeInsert()
      }
    }
  }

  def uninstall(oauthId: String)(implicit ec: ExecutionContext): Future[Unit] = {
    tenantFromCache(oauthId) match {
      case None => Future.successful(())
      case Some(tenant) => getToken(tenant).map(_ => ()).recover {
        case NonFatal(e) =>
          //Only do uninstall if we can NOT getToken
          cache.remove(oauthId)
          db.withConnection { implicit c =>
            SQL"DELETE FROM tenant WHERE id = $oauthId".executeUpdate()
          }
          ()
      }
    }
  }

  private def tenantFromCache(id: String): Option[Tenant] = cache.get[Tenant](s"tenant.$id")
  private def tokenFromCache(): Option[Token] = cache.get[Token](TokenCacheKey)

  private def getTenant(installData: HipchatInstallData)(implicit ec: ExecutionContext): Future[Tenant] = {
    tenantFromCache(installData.oauthId).map(Future.successful).getOrElse {
      val tenantFuture = ws.url(installData.capabilitiesUrl).get().map { response =>
        Tenant(installData, Json.fromJson[HipchatCapabilitiesDescriptor](response.json).get)
      }
      tenantFuture.onSuccess { case tenant =>
        cache.set(s"tenant.${installData.oauthId}", tenant)
      }
      tenantFuture
    }
  }

  private def getToken(tenant: Tenant)(implicit ec: ExecutionContext) = {
    tokenFromCache().map(Future.successful).getOrElse {
      val tokenFuture = ws.url(tenant.linkToken)
        .withHeaders(HeaderNames.CONTENT_TYPE -> MimeTypes.FORM)
        .withAuth(tenant.id, tenant.secret, WSAuthScheme.BASIC)
        .withQueryString(
          "grant_type" -> "client_credentials",
          "scope" -> Scopes.mkString(" ")
        ).execute("POST").map(_.json.as[Token])
      tokenFuture.onSuccess { case auth =>
        cache.set(TokenCacheKey, auth, (auth.expires_in - 60).seconds)
      }
      tokenFuture
    }

  }

}
