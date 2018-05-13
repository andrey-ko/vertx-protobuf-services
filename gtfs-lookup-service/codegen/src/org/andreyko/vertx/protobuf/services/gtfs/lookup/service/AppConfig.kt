package org.andreyko.vertx.protobuf.services.gtfs.lookup.service

import com.fasterxml.jackson.annotation.*

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
class AppConfig {
  @JsonProperty("--fds")
  var fds: String? = null
  
  @JsonProperty("--inc")
  var inc: String? = null
  
  @JsonProperty("--out")
  var out: String = ""
}


