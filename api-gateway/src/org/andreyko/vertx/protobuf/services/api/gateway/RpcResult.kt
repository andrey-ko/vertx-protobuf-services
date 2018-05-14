package org.andreyko.vertx.protobuf.services.api.gateway

import com.fasterxml.jackson.databind.*
import io.vertx.core.*

class RpcResult(
  val code: Int,
  val message: String,
  val content: JsonNode? = null,
  val headers: MultiMap? = null
)
