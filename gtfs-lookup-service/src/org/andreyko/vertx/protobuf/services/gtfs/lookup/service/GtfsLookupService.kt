package org.andreyko.vertx.protobuf.services.gtfs.lookup.service;

import com.google.protobuf.*
import com.satori.libs.async.kotlin.*
import org.slf4j.*

class GtfsLookupService : IGtfsLookup {
  val log = LoggerFactory.getLogger(javaClass)
  
  override fun status(msg: Empty) = future {
    log.info("status request received")
    return@future Status.newBuilder().apply {
      name = MetaInfo.project
      version = MetaInfo.version
      sha = MetaInfo.sha
      api = "v0"
    }.build()
  }
}