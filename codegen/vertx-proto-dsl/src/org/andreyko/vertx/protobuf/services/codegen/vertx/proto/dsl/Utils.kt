package org.andreyko.vertx.protobuf.services.codegen.vertx.proto.dsl

import com.satori.libs.common.kotlin.*
import java.io.*

fun writeJavaClass(outDir: String, pckg: String, className: String, block: (Writer) -> Unit) {
  val pckgDir = pckg.replace(".", "/")
  file("${outDir}/$pckgDir/${className}.java") {
    write { os ->
      os.writer().use { ow ->
        block(ow)
      }
    }
    println("file generated '${path.fileName}' ('${path}')")
  }
}
