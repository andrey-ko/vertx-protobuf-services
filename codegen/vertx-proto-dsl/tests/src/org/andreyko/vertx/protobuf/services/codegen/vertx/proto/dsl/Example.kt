package org.andreyko.vertx.protobuf.services.codegen.vertx.proto.dsl

import com.google.protobuf.*
import com.satori.libs.common.kotlin.*

object Example {
  
  @JvmStatic
  fun main(vararg args: String) {
    println("java version: ${System.getProperty("java.version")}")
    
    val fds = javaClass.classLoader.getResourceAsStream("grpc.pb").use { istream ->
      DescriptorProtos.FileDescriptorSet.parseFrom(istream)
    }
    val inc = javaClass.classLoader.getResourceAsStream("grpc-includes.pb").use { istream ->
      DescriptorProtos.FileDescriptorSet.parseFrom(istream)
    }
    CodeGenEnv.from(fds, inc) {
      for (fd in fds.fileList) {
        for (sd in fd.serviceList) {
          service(fd, sd) {
          
            println("// file: '$pckg.$apiType.java': ")
            System.out.nonClosableWrapper().writer().use { w ->
              ApiTemplate.render(w, generateApi())
            }
          
            println("// file: '$pckg.$moduleType.java': ")
            System.out.nonClosableWrapper().writer().use { w ->
              ModuleTemplate.render(w, generateModule())
            }
          
            println("// file: '$pckg.$stubType.java': ")
            System.out.nonClosableWrapper().writer().use { w ->
              StubTemplate.render(w, generateStub())
            }
          
            println("// file: '$pckg.$verticleType.java': ")
            System.out.nonClosableWrapper().writer().use { w ->
              VerticleTemplate.render(w, generateVerticle())
            }
          
          }
        }
      }
    }
  }
}