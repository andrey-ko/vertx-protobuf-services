package org.andreyko.vertx.protobuf.services.codegen.vertx.proto.dsl

import com.google.protobuf.DescriptorProtos.*
import com.satori.codegen.utils.*

fun ICodeGenSrv.generateApi(): ApiModel {
  val model = ApiModel()
  model.javaPckg = javaPckg
  model.apiType = apiType
  
  for (md in sd.methodList) {
    model.addOperationsItem().apply {
      name = md.name
      outputType = resolveType(md.outputType)
      inputType = resolveType(md.inputType)
    }
  }
  
  return model
}

fun ICodeGenSrv.generateModule(): ModuleModel {
  val model = ModuleModel()
  model.javaPckg = javaPckg
  model.moduleType = moduleType
  model.apiType = apiType
  model.proxyType = proxyType
  model.serviceAddress = serviceAddress
  
  return model
}

fun ICodeGenSrv.generateStub(): StubModel {
  val model = StubModel()
  model.javaPckg = javaPckg
  model.stubType = stubType
  model.apiType = apiType
  
  for (md in sd.methodList) {
    model.addOperationsItem().apply {
      funName = md.name.camelCase()
      opId = md.name.kebabCase()
      outputType = resolveType(md.outputType)
      inputType = resolveType(md.inputType)
    }
  }
  
  return model
}

fun ICodeGenSrv.generateProxy(): ProxyModel {
  val model = ProxyModel()
  model.javaPckg = javaPckg
  model.proxyType = proxyType
  model.apiType = apiType
  model.moduleType = moduleType
  
  for (md in sd.methodList) {
    model.addOperationsItem().apply {
      funName = md.name.camelCase()
      opId = md.name.kebabCase()
      outputType = resolveType(md.outputType)
      inputType = resolveType(md.inputType)
    }
  }
  
  return model
}

fun ICodeGenSrv.generateVerticle(): VerticleModel {
  val model = VerticleModel()
  
  model.javaPckg = javaPckg
  model.verticleType = verticleType
  model.apiType = apiType
  model.stubType = stubType
  model.moduleType = moduleType
  
  return model
}


fun ICodeGenEnv.resolveType(type: String): String {
  val typeInfo = types[type.removePrefix(".")]!!
  return "${typeInfo.javaPckg}.${typeInfo.descriptor.name}"
}

inline fun ICodeGenEnv.service(fd: FileDescriptorProto, sd: ServiceDescriptorProto, block:ICodeGenSrv.()->Unit) = service(fd, sd).apply(block)