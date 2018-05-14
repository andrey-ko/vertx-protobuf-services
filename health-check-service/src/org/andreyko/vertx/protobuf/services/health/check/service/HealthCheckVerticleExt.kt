// auto generated
// don't modify
package org.andreyko.vertx.protobuf.services.health.check.service

import javax.inject.*

import io.vertx.core.*
import io.vertx.core.buffer.*
import io.vertx.core.eventbus.*
import org.slf4j.*

class HealthCheckVerticleExt (val service: HealthCheckService, val address: String) : AbstractVerticle() {
  private val log = LoggerFactory.getLogger(javaClass)
  
  private var consumer: MessageConsumer<Buffer>? = null
  
  @Inject
  constructor(service: HealthCheckService) : this(service, HealthCheckModule.address) {
  }
  
  override fun start(future: Future<Void>) {
    log.info("started (deploymentId = \${deploymentID()})")

    service.start()
    
    // create handler for service
    val handler = HealthCheckMessageHandler(service)
    consumer = vertx.eventBus().consumer(address, handler)
    
    future.complete()
  }
  
  override fun stop(future: Future<Void>) {
    log.info("stopping verticle (deploymentId = \${deploymentID()})...")
    
    val consumer = this.consumer
    this.consumer = null
    
    if (consumer != null) {
      try {
        consumer.unregister()
      } catch (ex: Throwable) {
        // swallow exception
        log.error("failed to unregister message consumer", ex)
      }
      
    }
  
    service.stop()
    
    future.complete()
  }
}