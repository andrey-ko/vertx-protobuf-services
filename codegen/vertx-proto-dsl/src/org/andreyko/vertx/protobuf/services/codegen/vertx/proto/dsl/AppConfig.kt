package org.andreyko.vertx.protobuf.services.codegen.vertx.proto.dsl

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class AppConfig {
  @JsonProperty("--fds")
  var fds: String? = null
  
  @JsonProperty("--inc")
  var inc: String? = null
  
  @JsonProperty("--out")
  lateinit var out: String
}


