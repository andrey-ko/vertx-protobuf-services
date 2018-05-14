package org.andreyko.vertx.protobuf.services.codegen.vertx.proto.dsl

import com.google.protobuf.*
import com.google.protobuf.DescriptorProtos.*
import com.satori.codegen.utils.*

interface ICodeGenEnv {
  val fds: DescriptorProtos.FileDescriptorSet
  val inc: DescriptorProtos.FileDescriptorSet
  val fmt: ICodeFormatter get() = CodeFormatter
  val types: HashMap<String, TypeInfo>
  fun service(fd: FileDescriptorProto, sd: ServiceDescriptorProto): ICodeGenSrv
}
