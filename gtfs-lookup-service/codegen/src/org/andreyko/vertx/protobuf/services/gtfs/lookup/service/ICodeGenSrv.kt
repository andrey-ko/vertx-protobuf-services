package org.andreyko.vertx.protobuf.services.gtfs.lookup.service

import com.google.protobuf.DescriptorProtos.*

interface ICodeGenSrv : ICodeGenEnv {
  val fd: FileDescriptorProto
  val sd: ServiceDescriptorProto
  val pckg: String
  val javaPckg: String
  val stubType: String
  val verticleType: String
  val apiType: String
  val proxyType: String
  val moduleType: String
  val serviceAddress: String
}
