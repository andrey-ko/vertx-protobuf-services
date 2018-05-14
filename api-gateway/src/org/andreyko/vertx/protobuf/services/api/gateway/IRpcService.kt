package org.andreyko.vertx.protobuf.services.api.gateway

import com.satori.libs.async.api.*

interface IRpcService {
  fun handle(request: RpcRequest): IAsyncFuture<RpcResult>
}
