package org.andreyko.vertx.protobuf.services.codegen.vertx.proto.dsl

import com.google.protobuf.compiler.*
import com.satori.libs.common.kotlin.*

object CodeGenApp {
  @JvmStatic
  fun main(vararg args: String) {
    //println("hello world!")
  
    val request = PluginProtos.CodeGeneratorRequest.parseFrom(System.`in`)
    
    
    val response = PluginProtos.CodeGeneratorResponse.newBuilder()
      .build()
    //generate_code(request, response)
  
    response.writeTo(System.out)
    
    file("!test.txt"){
      write("hello world!")
    }
  }
}