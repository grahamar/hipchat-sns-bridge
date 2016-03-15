package com.teambytes.hipchat.sns.bridge.data.models

import play.api.libs.json.Json

case class Token(access_token: String,
                 expires_in: Int,
                 group_name: String,
                 token_type: String,
                 scope: String,
                 group_id: Int)

object Token {
  implicit val reads = Json.reads[Token]
}
