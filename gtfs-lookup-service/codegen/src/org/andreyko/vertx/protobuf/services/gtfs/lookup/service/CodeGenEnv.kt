package org.andreyko.vertx.protobuf.services.gtfs.lookup.service

import com.google.protobuf.DescriptorProtos.*
import com.satori.codegen.utils.*
import com.satori.libs.common.kotlin.*

class CodeGenEnv(
  override val fds: FileDescriptorSet,
  override val inc: FileDescriptorSet
) : ICodeGenEnv {
  
  override val fmt: ICodeFormatter get() = CodeFormatter
  override val types = HashMap<String, TypeInfo>()
  
  init {
    loadTypesFrom(fds)
    loadTypesFrom(inc)
  }
  
  fun loadTypesFrom(fds: FileDescriptorSet) {
    for (fd in fds.fileList) {
      val pckg = fd.`package`!!
      val javaPckg = fd.options.javaPackage ?: pckg
      for (t in fd.messageTypeList) {
        types.put("$pckg.${t.name}", TypeInfo(javaPckg, t))
      }
    }
  }
  
  override fun service(fd: FileDescriptorProto, sd: ServiceDescriptorProto): ICodeGenSrv {
    return object: ICodeGenSrv, ICodeGenEnv by this {
      override val fd = fd
      override val sd = sd
      override val pckg = fd.`package`
      override val javaPckg = fd.options.javaPackage ?: pckg
      override val stubType = "${sd.name.pascalCase()}MessageHandler"
      override val verticleType = "${sd.name.pascalCase()}Verticle"
      override val apiType = "I${sd.name.pascalCase()}"
      override val proxyType = "${sd.name.pascalCase()}Proxy"
      override val moduleType = "${sd.name.pascalCase()}Module"
      override val serviceAddress = "${sd.name.kebabCase()}-service"
    }
  }
  
  companion object {
    inline fun from(fds: FileDescriptorSet, inc: FileDescriptorSet, block: CodeGenEnv.() -> Unit): CodeGenEnv = from(fds, inc).apply(block)
    fun from(fds: FileDescriptorSet, inc: FileDescriptorSet) = CodeGenEnv(fds, inc)
    
    inline fun from(cfg: AppConfig, block: CodeGenEnv.() -> Unit): CodeGenEnv = from(cfg).apply(block)
    fun from(cfg: AppConfig): CodeGenEnv {
      val fds = file(cfg.fds!!) {
        read { istream ->
          FileDescriptorSet.parseFrom(istream)
        }
      }
      val inc = file(cfg.inc!!) {
        read { istream ->
          FileDescriptorSet.parseFrom(istream)
        }
      }
      return from(fds, inc)
    }
  }
}
