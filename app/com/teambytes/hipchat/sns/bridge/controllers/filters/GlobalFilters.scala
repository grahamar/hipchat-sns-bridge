package com.teambytes.hipchat.sns.bridge.controllers.filters

import javax.inject.{Inject, Singleton}

import play.api.http.HttpFilters
import play.api.mvc.EssentialFilter
import play.filters.cors.CORSFilter
import play.filters.csrf.CSRFFilter
import play.filters.headers.SecurityHeadersFilter

@Singleton
class GlobalFilters @Inject() (loggingFilter: LoggingFilter,
                               cSRFFilter: CSRFFilter,
                               corsFilter: CORSFilter,
                               securityHeadersFilter: SecurityHeadersFilter) extends HttpFilters {
  override val filters: Seq[EssentialFilter] = Seq(securityHeadersFilter, corsFilter, cSRFFilter, loggingFilter)
}
