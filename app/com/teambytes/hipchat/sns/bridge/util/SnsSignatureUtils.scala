package com.teambytes.hipchat.sns.bridge.util

import java.net.URL
import java.security.Signature
import java.security.cert.{X509Certificate, CertificateFactory}

import com.teambytes.hipchat.sns.bridge.v0.models.SnsNotification
import org.apache.commons.codec.binary.Base64

import scala.util.Try
import scala.util.control.NonFatal

object SnsSignatureUtils {

  def isValidSnsMessage(msg: SnsNotification): Boolean = {
    Try {
      val url = new URL(msg.SigningCertURL)
      val inStream = url.openStream()
      val cf = CertificateFactory.getInstance("X.509")
      val cert = cf.generateCertificate(inStream).asInstanceOf[X509Certificate]
      val sig = Signature.getInstance("SHA1withRSA")
      sig.initVerify(cert.getPublicKey)
      sig.update(messageBytesToSign(msg))
      sig.verify(Base64.decodeBase64(msg.Signature))
    }.recover {
      case NonFatal(e) =>
        // TODO log failure
        false
    }.get
  }

  private def messageBytesToSign(msg: SnsNotification): Array[Byte] = {
    msg.Type match {
      case "Notification" => buildNotificationStringToSign(msg).getBytes()
      case "SubscriptionConfirmation" | "UnsubscribeConfirmation" => buildSubscriptionStringToSign(msg).getBytes()
    }
  }

  /**
    * Build the string to sign for Notification messages from the values in the message.
    * Name and values separated by newline characters
    * The name value pairs are sorted by name in byte sort order.
    */
  private def buildNotificationStringToSign(msg: SnsNotification): String = {
    val subject = msg.Subject.map { subject =>
      s"""
        |Subject
        |$subject
      """.stripMargin
    }.getOrElse("")

    s"""Message
       |${msg.Message}
       |MessageId
       |${msg.MessageId}
       |$subject
       |Timestamp
       |${msg.Timestamp}
       |TopicArn
       |${msg.TopicArn}
       |Type
       |${msg.Type}
       |
     """.stripMargin
  }

  /**
    * Build the string to sign for SubscriptionConfirmation and UnsubscribeConfirmation messages from the values in the message.
    * Name and values separated by newline characters
    * The name value pairs are sorted by name in byte sort order.
    */
  private def buildSubscriptionStringToSign(msg: SnsNotification): String = {
    s"""Message
        |${msg.Message}
        |MessageId
        |${msg.MessageId}
        |SubscribeURL
        |${msg.SubscribeURL.getOrElse("")}
        |Timestamp
        |${msg.Timestamp}
        |Token
        |${msg.Token}
        |TopicArn
        |${msg.TopicArn}
        |Type
        |${msg.Type}
        |
     """.stripMargin
  }

}
