package org.andreyko.vertx.protobuf.services.api.gateway

fun RpcResult.isSucceeded() = code in RpcResults.CODE_2xx
fun RpcResult.isNotFound() = code == RpcResults.CODE_NOT_FOUND

