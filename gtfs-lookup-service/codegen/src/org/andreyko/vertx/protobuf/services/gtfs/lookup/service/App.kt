@file: JvmName("App")

package org.andreyko.vertx.protobuf.services.gtfs.lookup.service

import com.satori.codegen.utils.*
import com.satori.libs.common.kotlin.json.*

object App : IJsonContext by DefaultJsonContext {
  
  @JvmStatic
  fun main(vararg args: String) {
    println("service-stubs-gen version: 0.0")
    
    val cfg = jsonObject {
      val itor = args.iterator()
      while (true) {
        val name = if (itor.hasNext()) itor.next() else break
        val value = if (itor.hasNext()) itor.next() else throw Exception("argument value is missing")
        if (!name.startsWith("--")) throw Exception("argument '$name' should start with '--'")
        field(name, value)
      }
    }.scope { toValue<AppConfig>() }
    
    CodeGenEnv.from(cfg) {
      for (fd in fds.fileList) {
        val pckg = fd.`package`
        val javaPckg = fd.options.javaPackage ?: pckg
        for (sd in fd.serviceList) {
          service(fd, sd){
            writeJavaClass("${cfg.out}/api", javaPckg, apiType) { w ->
              ApiTemplate.render(w, generateApi())
            }
            writeJavaClass("${cfg.out}/stubs", javaPckg, moduleType) { w ->
              ModuleTemplate.render(w, generateModule())
            }
            writeJavaClass("${cfg.out}/stubs", javaPckg, proxyType) { w ->
              ProxyTemplate.render(w, generateProxy())
            }
            writeJavaClass("${cfg.out}/stubs", javaPckg, stubType) { w ->
              StubTemplate.render(w, generateStub())
            }
            writeJavaClass("${cfg.out}/stubs", javaPckg, verticleType) { w ->
              VerticleTemplate.render(w, generateVerticle())
            }
          }
        }
      }
    }
  }
  
}
