package com.teambytes.hipchat.sns.bridge.util

import java.net.URL
import java.security.cert.{CertificateFactory, X509Certificate}

import com.amazonaws.services.sns.util.SignatureChecker
import com.teambytes.hipchat.sns.bridge.v0.models.json._
import com.teambytes.hipchat.sns.bridge.v0.models.SnsNotification
import play.api.Logger
import play.api.libs.json.Json

import scala.util.Try
import scala.util.control.NonFatal

object SnsSignatureUtils {

  def isValidSnsMessage(msg: SnsNotification): Boolean = {
    Try {
      val url = new URL(msg.SigningCertURL)
      val inStream = url.openStream()
      val cf = CertificateFactory.getInstance("X.509")
      val cert = cf.generateCertificate(inStream).asInstanceOf[X509Certificate]
      new SignatureChecker().verifyMessageSignature(Json.toJson(msg).toString, cert.getPublicKey)
    }.recover {
      case NonFatal(e) =>
        Logger.error(s"Exception validating SNS message signature", e)
        false
    }.get
  }

}
