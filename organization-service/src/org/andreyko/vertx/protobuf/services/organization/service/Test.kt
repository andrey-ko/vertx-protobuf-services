package org.andreyko.vertx.protobuf.services.organization.service

import com.google.protobuf.*
import com.hazelcast.config.*
import io.vertx.core.*
import io.vertx.spi.cluster.hazelcast.*
import org.slf4j.*

object Test {
  val log = LoggerFactory.getLogger(javaClass)
  
  @JvmStatic
  fun main(vararg args: String) {
    val hzCfg = Config()
    //hzCfg.setProperty("hazelcast.logging.type", "slf4j");
    System.setProperty("vertx.hazelcast.async-api", "true")
    
    val clusterManager = HazelcastClusterManager(hzCfg)
    val vertxOptions = VertxOptions().apply {
      this.clusterManager = clusterManager
      this.isClustered = true
    }
    
    Vertx.clusteredVertx(vertxOptions) { ar ->
      if (!ar.succeeded()) {
        log.error("failed to create vertx", ar.cause())
        return@clusteredVertx
      }
      val vertx = ar.result()
      log.info("vertx successfully created")
      
      
      vertx.runOnContext {
        val service = OrganizationProxy(vertx)
        service.status(Empty.getDefaultInstance()).onCompleted {ar ->
          if(!ar.isSucceeded) {
            log.error("request failed", ar.error)
          }else{
            println(ar.value)
          }
          vertx.close()
        }
      }
      
    }
  }
}