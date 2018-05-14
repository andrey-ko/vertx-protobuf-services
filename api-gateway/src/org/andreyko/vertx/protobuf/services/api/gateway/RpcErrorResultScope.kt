package org.andreyko.vertx.protobuf.services.api.gateway

class RpcErrorResultScope : RpcNoContentScope() {
  
  var error: String? = null
  
  fun error(value: String) {
    error = value
  }
}
