package org.andreyko.vertx.protobuf.services.gtfs.lookup.service

import com.google.protobuf.*
import com.google.protobuf.DescriptorProtos.*
import com.satori.codegen.utils.*
import com.satori.libs.common.kotlin.*

interface ICodeGenEnv {
  val fds: DescriptorProtos.FileDescriptorSet
  val inc: DescriptorProtos.FileDescriptorSet
  val fmt: ICodeFormatter get() = CodeFormatter
  val types: HashMap<String, TypeInfo>
  fun service(fd: FileDescriptorProto, sd: ServiceDescriptorProto): ICodeGenSrv
}
