/**
  * Generated by apidoc - http://www.apidoc.me
  * Service version: 0.0.1-dev
  * apidoc:0.11.17 http://www.apidoc.me/teambytes/hipchat-sns-bridge/0.0.1-dev/play_2_x_standalone_json
  */
package com.teambytes.hipchat.sns.bridge.v0.models {

  /**
    * A request semantic failure reason.
    */
  case class Error(
                    description: String
                  )

  case class HipchatApiConsumer(
                                 scopes: Seq[com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope],
                                 fromName: _root_.scala.Option[String] = None
                               )

  case class HipchatCapabilities(
                                  configurable: _root_.scala.Option[com.teambytes.hipchat.sns.bridge.v0.models.HipchatConfigurationPage] = None,
                                  hipchatApiConsumer: _root_.scala.Option[com.teambytes.hipchat.sns.bridge.v0.models.HipchatApiConsumer] = None,
                                  installable: _root_.scala.Option[com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallable] = None,
                                  oauth2Provider: _root_.scala.Option[com.teambytes.hipchat.sns.bridge.v0.models.HipchatOauth2Provider] = None
                                )

  case class HipchatCapabilitiesDescriptor(
                                            name: String,
                                            description: String,
                                            key: String,
                                            apiVersion: _root_.scala.Option[String] = None,
                                            capabilities: _root_.scala.Option[com.teambytes.hipchat.sns.bridge.v0.models.HipchatCapabilities] = None,
                                            links: com.teambytes.hipchat.sns.bridge.v0.models.HipchatLinks,
                                            vendor: _root_.scala.Option[com.teambytes.hipchat.sns.bridge.v0.models.HipchatVendor] = None
                                          )

  case class HipchatConfigurationPage(
                                       allowAccessToRoomAdmins: _root_.scala.Option[String] = None,
                                       url: String
                                     )

  case class HipchatInstallData(
                                 capabilitiesUrl: String,
                                 oauthId: String,
                                 oauthSecret: String,
                                 groupId: Long,
                                 roomId: _root_.scala.Option[String] = None
                               )

  case class HipchatInstallable(
                                 callbackUrl: _root_.scala.Option[String] = None,
                                 installedUrl: _root_.scala.Option[String] = None,
                                 allowGlobal: _root_.scala.Option[Boolean] = None,
                                 allowRoom: _root_.scala.Option[Boolean] = None
                               )

  case class HipchatLinks(
                           homepage: _root_.scala.Option[String] = None,
                           api: _root_.scala.Option[String] = None,
                           self: String
                         )

  case class HipchatOauth2Provider(
                                    tokenUrl: _root_.scala.Option[String] = None,
                                    authorizationUrl: _root_.scala.Option[String] = None
                                  )

  case class HipchatVendor(
                            name: String,
                            url: String
                          )

  case class ServiceInfo(
                          name: String,
                          version: String,
                          scalaVersion: String,
                          sbtVersion: String,
                          buildTime: _root_.org.joda.time.DateTime
                        )

  case class SnsNotification(
                              Type: String,
                              MessageId: String,
                              TopicArn: String,
                              Message: String,
                              Timestamp: String,
                              SignatureVersion: String,
                              Signature: String,
                              SigningCertURL: String,
                              Token: _root_.scala.Option[String] = None,
                              Subject: _root_.scala.Option[String] = None,
                              SubscribeURL: _root_.scala.Option[String] = None,
                              UnsubscribeURL: _root_.scala.Option[String] = None
                            )

  /**
    * Your add-on must declare the scopes it requires based on which API endpoints it
    * needs to use, via its descriptor.
    */
  sealed trait HipchatScope

  object HipchatScope {

    case object SendMessage extends HipchatScope { override def toString = "send_message" }
    case object SendNotification extends HipchatScope { override def toString = "send_notification" }

    /**
      * UNDEFINED captures values that are sent either in error or
      * that were added by the server after this library was
      * generated. We want to make it easy and obvious for users of
      * this library to handle this case gracefully.
      *
      * We use all CAPS for the variable name to avoid collisions
      * with the camel cased values above.
      */
    case class UNDEFINED(override val toString: String) extends HipchatScope

    /**
      * all returns a list of all the valid, known values. We use
      * lower case to avoid collisions with the camel cased values
      * above.
      */
    val all = Seq(SendMessage, SendNotification)

    private[this]
    val byName = all.map(x => x.toString.toLowerCase -> x).toMap

    def apply(value: String): HipchatScope = fromString(value).getOrElse(UNDEFINED(value))

    def fromString(value: String): _root_.scala.Option[HipchatScope] = byName.get(value.toLowerCase)

  }

}

package com.teambytes.hipchat.sns.bridge.v0.models {

  package object json {
    import play.api.libs.json.__
    import play.api.libs.json.JsString
    import play.api.libs.json.Writes
    import play.api.libs.functional.syntax._
    import com.teambytes.hipchat.sns.bridge.v0.models.json._

    private[v0] implicit val jsonReadsUUID = __.read[String].map(java.util.UUID.fromString)

    private[v0] implicit val jsonWritesUUID = new Writes[java.util.UUID] {
      def writes(x: java.util.UUID) = JsString(x.toString)
    }

    private[v0] implicit val jsonReadsJodaDateTime = __.read[String].map { str =>
      import org.joda.time.format.ISODateTimeFormat.dateTimeParser
      dateTimeParser.parseDateTime(str)
    }

    private[v0] implicit val jsonWritesJodaDateTime = new Writes[org.joda.time.DateTime] {
      def writes(x: org.joda.time.DateTime) = {
        import org.joda.time.format.ISODateTimeFormat.dateTime
        val str = dateTime.print(x)
        JsString(str)
      }
    }

    implicit val jsonReadsHipchatSnsBridgeHipchatScope = new play.api.libs.json.Reads[com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope] {
      def reads(js: play.api.libs.json.JsValue): play.api.libs.json.JsResult[com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope] = {
        js match {
          case v: play.api.libs.json.JsString => play.api.libs.json.JsSuccess(com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope(v.value))
          case _ => {
            (js \ "value").validate[String] match {
              case play.api.libs.json.JsSuccess(v, _) => play.api.libs.json.JsSuccess(com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope(v))
              case err: play.api.libs.json.JsError => err
            }
          }
        }
      }
    }

    def jsonWritesHipchatSnsBridgeHipchatScope(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope) = {
      play.api.libs.json.JsString(obj.toString)
    }

    def jsObjectHipchatScope(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope) = {
      play.api.libs.json.Json.obj("value" -> play.api.libs.json.JsString(obj.toString))
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatScope: play.api.libs.json.Writes[HipchatScope] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope) = {
          jsonWritesHipchatSnsBridgeHipchatScope(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeError: play.api.libs.json.Reads[Error] = {
      (__ \ "description").read[String].map { x => new Error(description = x) }
    }

    def jsObjectError(obj: com.teambytes.hipchat.sns.bridge.v0.models.Error) = {
      play.api.libs.json.Json.obj(
        "description" -> play.api.libs.json.JsString(obj.description)
      )
    }

    implicit def jsonWritesHipchatSnsBridgeError: play.api.libs.json.Writes[Error] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.Error] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.Error) = {
          jsObjectError(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatApiConsumer: play.api.libs.json.Reads[HipchatApiConsumer] = {
      (
        (__ \ "scopes").read[Seq[com.teambytes.hipchat.sns.bridge.v0.models.HipchatScope]] and
          (__ \ "fromName").readNullable[String]
        )(HipchatApiConsumer.apply _)
    }

    def jsObjectHipchatApiConsumer(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatApiConsumer) = {
      play.api.libs.json.Json.obj(
        "scopes" -> play.api.libs.json.Json.toJson(obj.scopes)
      ) ++ (obj.fromName match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("fromName" -> play.api.libs.json.JsString(x))
      })
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatApiConsumer: play.api.libs.json.Writes[HipchatApiConsumer] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatApiConsumer] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatApiConsumer) = {
          jsObjectHipchatApiConsumer(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatCapabilities: play.api.libs.json.Reads[HipchatCapabilities] = {
      (
        (__ \ "configurable").readNullable[com.teambytes.hipchat.sns.bridge.v0.models.HipchatConfigurationPage] and
          (__ \ "hipchatApiConsumer").readNullable[com.teambytes.hipchat.sns.bridge.v0.models.HipchatApiConsumer] and
          (__ \ "installable").readNullable[com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallable] and
          (__ \ "oauth2Provider").readNullable[com.teambytes.hipchat.sns.bridge.v0.models.HipchatOauth2Provider]
        )(HipchatCapabilities.apply _)
    }

    def jsObjectHipchatCapabilities(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatCapabilities) = {
      (obj.configurable match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("configurable" -> jsObjectHipchatConfigurationPage(x))
      }) ++
        (obj.hipchatApiConsumer match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("hipchatApiConsumer" -> jsObjectHipchatApiConsumer(x))
        }) ++
        (obj.installable match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("installable" -> jsObjectHipchatInstallable(x))
        }) ++
        (obj.oauth2Provider match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("oauth2Provider" -> jsObjectHipchatOauth2Provider(x))
        })
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatCapabilities: play.api.libs.json.Writes[HipchatCapabilities] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatCapabilities] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatCapabilities) = {
          jsObjectHipchatCapabilities(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatCapabilitiesDescriptor: play.api.libs.json.Reads[HipchatCapabilitiesDescriptor] = {
      (
        (__ \ "name").read[String] and
          (__ \ "description").read[String] and
          (__ \ "key").read[String] and
          (__ \ "apiVersion").readNullable[String] and
          (__ \ "capabilities").readNullable[com.teambytes.hipchat.sns.bridge.v0.models.HipchatCapabilities] and
          (__ \ "links").read[com.teambytes.hipchat.sns.bridge.v0.models.HipchatLinks] and
          (__ \ "vendor").readNullable[com.teambytes.hipchat.sns.bridge.v0.models.HipchatVendor]
        )(HipchatCapabilitiesDescriptor.apply _)
    }

    def jsObjectHipchatCapabilitiesDescriptor(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatCapabilitiesDescriptor) = {
      play.api.libs.json.Json.obj(
        "name" -> play.api.libs.json.JsString(obj.name),
        "description" -> play.api.libs.json.JsString(obj.description),
        "key" -> play.api.libs.json.JsString(obj.key),
        "links" -> jsObjectHipchatLinks(obj.links)
      ) ++ (obj.apiVersion match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("apiVersion" -> play.api.libs.json.JsString(x))
      }) ++
        (obj.capabilities match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("capabilities" -> jsObjectHipchatCapabilities(x))
        }) ++
        (obj.vendor match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("vendor" -> jsObjectHipchatVendor(x))
        })
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatCapabilitiesDescriptor: play.api.libs.json.Writes[HipchatCapabilitiesDescriptor] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatCapabilitiesDescriptor] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatCapabilitiesDescriptor) = {
          jsObjectHipchatCapabilitiesDescriptor(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatConfigurationPage: play.api.libs.json.Reads[HipchatConfigurationPage] = {
      (
        (__ \ "allowAccessToRoomAdmins").readNullable[String] and
          (__ \ "url").read[String]
        )(HipchatConfigurationPage.apply _)
    }

    def jsObjectHipchatConfigurationPage(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatConfigurationPage) = {
      play.api.libs.json.Json.obj(
        "url" -> play.api.libs.json.JsString(obj.url)
      ) ++ (obj.allowAccessToRoomAdmins match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("allowAccessToRoomAdmins" -> play.api.libs.json.JsString(x))
      })
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatConfigurationPage: play.api.libs.json.Writes[HipchatConfigurationPage] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatConfigurationPage] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatConfigurationPage) = {
          jsObjectHipchatConfigurationPage(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatInstallData: play.api.libs.json.Reads[HipchatInstallData] = {
      (
        (__ \ "capabilitiesUrl").read[String] and
          (__ \ "oauthId").read[String] and
          (__ \ "oauthSecret").read[String] and
          (__ \ "groupId").read[Long] and
          (__ \ "roomId").readNullable[String]
        )(HipchatInstallData.apply _)
    }

    def jsObjectHipchatInstallData(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallData) = {
      play.api.libs.json.Json.obj(
        "capabilitiesUrl" -> play.api.libs.json.JsString(obj.capabilitiesUrl),
        "oauthId" -> play.api.libs.json.JsString(obj.oauthId),
        "oauthSecret" -> play.api.libs.json.JsString(obj.oauthSecret),
        "groupId" -> play.api.libs.json.JsNumber(obj.groupId)
      ) ++ (obj.roomId match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("roomId" -> play.api.libs.json.JsString(x))
      })
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatInstallData: play.api.libs.json.Writes[HipchatInstallData] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallData] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallData) = {
          jsObjectHipchatInstallData(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatInstallable: play.api.libs.json.Reads[HipchatInstallable] = {
      (
        (__ \ "callbackUrl").readNullable[String] and
          (__ \ "installedUrl").readNullable[String] and
          (__ \ "allowGlobal").readNullable[Boolean] and
          (__ \ "allowRoom").readNullable[Boolean]
        )(HipchatInstallable.apply _)
    }

    def jsObjectHipchatInstallable(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallable) = {
      (obj.callbackUrl match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("callbackUrl" -> play.api.libs.json.JsString(x))
      }) ++
        (obj.installedUrl match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("installedUrl" -> play.api.libs.json.JsString(x))
        }) ++
        (obj.allowGlobal match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("allowGlobal" -> play.api.libs.json.JsBoolean(x))
        }) ++
        (obj.allowRoom match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("allowRoom" -> play.api.libs.json.JsBoolean(x))
        })
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatInstallable: play.api.libs.json.Writes[HipchatInstallable] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallable] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatInstallable) = {
          jsObjectHipchatInstallable(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatLinks: play.api.libs.json.Reads[HipchatLinks] = {
      (
        (__ \ "homepage").readNullable[String] and
          (__ \ "api").readNullable[String] and
          (__ \ "self").read[String]
        )(HipchatLinks.apply _)
    }

    def jsObjectHipchatLinks(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatLinks) = {
      play.api.libs.json.Json.obj(
        "self" -> play.api.libs.json.JsString(obj.self)
      ) ++ (obj.homepage match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("homepage" -> play.api.libs.json.JsString(x))
      }) ++
        (obj.api match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("api" -> play.api.libs.json.JsString(x))
        })
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatLinks: play.api.libs.json.Writes[HipchatLinks] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatLinks] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatLinks) = {
          jsObjectHipchatLinks(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatOauth2Provider: play.api.libs.json.Reads[HipchatOauth2Provider] = {
      (
        (__ \ "tokenUrl").readNullable[String] and
          (__ \ "authorizationUrl").readNullable[String]
        )(HipchatOauth2Provider.apply _)
    }

    def jsObjectHipchatOauth2Provider(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatOauth2Provider) = {
      (obj.tokenUrl match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("tokenUrl" -> play.api.libs.json.JsString(x))
      }) ++
        (obj.authorizationUrl match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("authorizationUrl" -> play.api.libs.json.JsString(x))
        })
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatOauth2Provider: play.api.libs.json.Writes[HipchatOauth2Provider] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatOauth2Provider] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatOauth2Provider) = {
          jsObjectHipchatOauth2Provider(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeHipchatVendor: play.api.libs.json.Reads[HipchatVendor] = {
      (
        (__ \ "name").read[String] and
          (__ \ "url").read[String]
        )(HipchatVendor.apply _)
    }

    def jsObjectHipchatVendor(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatVendor) = {
      play.api.libs.json.Json.obj(
        "name" -> play.api.libs.json.JsString(obj.name),
        "url" -> play.api.libs.json.JsString(obj.url)
      )
    }

    implicit def jsonWritesHipchatSnsBridgeHipchatVendor: play.api.libs.json.Writes[HipchatVendor] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.HipchatVendor] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.HipchatVendor) = {
          jsObjectHipchatVendor(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeServiceInfo: play.api.libs.json.Reads[ServiceInfo] = {
      (
        (__ \ "name").read[String] and
          (__ \ "version").read[String] and
          (__ \ "scala_version").read[String] and
          (__ \ "sbt_version").read[String] and
          (__ \ "build_time").read[_root_.org.joda.time.DateTime]
        )(ServiceInfo.apply _)
    }

    def jsObjectServiceInfo(obj: com.teambytes.hipchat.sns.bridge.v0.models.ServiceInfo) = {
      play.api.libs.json.Json.obj(
        "name" -> play.api.libs.json.JsString(obj.name),
        "version" -> play.api.libs.json.JsString(obj.version),
        "scala_version" -> play.api.libs.json.JsString(obj.scalaVersion),
        "sbt_version" -> play.api.libs.json.JsString(obj.sbtVersion),
        "build_time" -> play.api.libs.json.JsString(_root_.org.joda.time.format.ISODateTimeFormat.dateTime.print(obj.buildTime))
      )
    }

    implicit def jsonWritesHipchatSnsBridgeServiceInfo: play.api.libs.json.Writes[ServiceInfo] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.ServiceInfo] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.ServiceInfo) = {
          jsObjectServiceInfo(obj)
        }
      }
    }

    implicit def jsonReadsHipchatSnsBridgeSnsNotification: play.api.libs.json.Reads[SnsNotification] = {
      (
        (__ \ "Type").read[String] and
          (__ \ "MessageId").read[String] and
          (__ \ "TopicArn").read[String] and
          (__ \ "Message").read[String] and
          (__ \ "Timestamp").read[String] and
          (__ \ "SignatureVersion").read[String] and
          (__ \ "Signature").read[String] and
          (__ \ "SigningCertURL").read[String] and
          (__ \ "Token").readNullable[String] and
          (__ \ "Subject").readNullable[String] and
          (__ \ "SubscribeURL").readNullable[String] and
          (__ \ "UnsubscribeURL").readNullable[String]
        )(SnsNotification.apply _)
    }

    def jsObjectSnsNotification(obj: com.teambytes.hipchat.sns.bridge.v0.models.SnsNotification) = {
      play.api.libs.json.Json.obj(
        "Type" -> play.api.libs.json.JsString(obj.Type),
        "MessageId" -> play.api.libs.json.JsString(obj.MessageId),
        "TopicArn" -> play.api.libs.json.JsString(obj.TopicArn),
        "Message" -> play.api.libs.json.JsString(obj.Message),
        "Timestamp" -> play.api.libs.json.JsString(obj.Timestamp),
        "SignatureVersion" -> play.api.libs.json.JsString(obj.SignatureVersion),
        "Signature" -> play.api.libs.json.JsString(obj.Signature),
        "SigningCertURL" -> play.api.libs.json.JsString(obj.SigningCertURL)
      ) ++ (obj.Token match {
        case None => play.api.libs.json.Json.obj()
        case Some(x) => play.api.libs.json.Json.obj("Token" -> play.api.libs.json.JsString(x))
      }) ++
        (obj.Subject match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("Subject" -> play.api.libs.json.JsString(x))
        }) ++
        (obj.SubscribeURL match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("SubscribeURL" -> play.api.libs.json.JsString(x))
        }) ++
        (obj.UnsubscribeURL match {
          case None => play.api.libs.json.Json.obj()
          case Some(x) => play.api.libs.json.Json.obj("UnsubscribeURL" -> play.api.libs.json.JsString(x))
        })
    }

    implicit def jsonWritesHipchatSnsBridgeSnsNotification: play.api.libs.json.Writes[SnsNotification] = {
      new play.api.libs.json.Writes[com.teambytes.hipchat.sns.bridge.v0.models.SnsNotification] {
        def writes(obj: com.teambytes.hipchat.sns.bridge.v0.models.SnsNotification) = {
          jsObjectSnsNotification(obj)
        }
      }
    }
  }
}
