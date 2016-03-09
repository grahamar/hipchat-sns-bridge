package com.teambytes.hipchat.sns.bridge.controllers.filters

import java.net.InetAddress
import javax.inject.Singleton

import play.api.Logger
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

import scala.concurrent.Future

@Singleton
class LoggingFilter extends Filter {

  private val logger = Logger("requests")
  private val IgnoreUserAgent = "ELB-HealthChecker/1.0"
  private lazy val HostName = InetAddress.getLocalHost.getHostName

  def apply(nextFilter: (RequestHeader) => Future[Result])(requestHeader: RequestHeader): Future[Result] = {
    // We don't want to log requests from certain UserAgents (e.g. AWS ELB Healthchecker, otherwise our ELB fills our logs with junk)
    if(!requestHeader.headers.get("User-Agent").contains(IgnoreUserAgent)) {
      val startTime = System.currentTimeMillis
      logger.info(s"[${requestHeader.remoteAddress}] - ${requestHeader.method} ${requestHeader.uri}: start request - Request Headers: ${requestHeader.headers}")
      nextFilter(requestHeader).map { result =>
        val endTime = System.currentTimeMillis
        val requestTime = endTime - startTime
        logger.info(s"[${requestHeader.remoteAddress}] - ${requestHeader.method} ${requestHeader.uri} took ${requestTime}ms and returned ${result.header.status}")
        result.withHeaders("Request-Time" -> s"$requestTime ms").withHeaders("Request-Processed-By" -> HostName)
      }
    } else {
      nextFilter(requestHeader)
    }
  }

}
