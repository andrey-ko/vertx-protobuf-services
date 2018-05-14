package org.andreyko.vertx.protobuf.services.health.check.service;

import com.google.protobuf.*
import com.google.protobuf.Message
import com.hazelcast.core.*
import com.hazelcast.core.MultiMap
import com.satori.libs.async.core.*
import com.satori.libs.async.kotlin.*
import io.vertx.core.*
import org.andreyko.vertx.protobuf.services.gtfs.lookup.service.*
import org.andreyko.vertx.protobuf.services.organization.service.*
import org.slf4j.*
import java.util.*
import javax.inject.*

class HealthCheckService @Inject constructor(
  val hz: HazelcastInstance,
  val vertx: Vertx,
  val gtfsLookupService: IGtfsLookup,
  val organizationService: IOrganization
) : IHealthCheck {
  
  val log = LoggerFactory.getLogger(javaClass)
  val delay = 1000L
  var timerId = INVALID_TIMER_ID
  val status = HashMap<String, String>()
  
  fun start() {
    status.clear()
    timerId = vertx.setTimer(delay) { doCheck() }
  }
  
  fun stop() {
    if (timerId != INVALID_TIMER_ID) {
      vertx.cancelTimer(timerId)
      timerId = INVALID_TIMER_ID
    }
  }
  
  override fun status(msg: Empty) = future {
    log.info("status request received")
    return@future Status.newBuilder().apply {
      name = MetaInfo.project
      version = MetaInfo.version
      sha = MetaInfo.sha
      api = "v0"
    }.build()
  }
  override fun getServiceStatuses(msg: Empty) = future {
    val repl = ServiceStatuses.newBuilder()
    status.forEach { t, u ->
      repl.addServiceStatus(ServiceStatus.newBuilder()
        .setName(t)
        .setStatus(u)
      )
    }
    return@future repl.build()
  }
  
  /*override fun getServiceStatuses(resultHandler: VxAsyncHandler<List<ServiceStatus>>) {
    resultHandler.handle(Future.succeededFuture(status.mapTo(ArrayList()) { (k, v) ->
      ServiceStatus().apply {
        serviceName = k
        status = v
      }
    }))
  }*/
  
  /* override fun clusterInfo(resultHandler: VxAsyncHandler<ClusterInfo>) {
     val hzCluster = hz.cluster
     val members = ArrayList<ClusterMemberInfo>()
     for (hzMember in hzCluster.members) {
       members.addNew {
         address = hzMember.address.toString()
         uuid = hzMember.uuid
         version = hzMember.version.toString()
       }
     }
     resultHandler.handle(Future.succeededFuture(ClusterInfo().apply {
       this.members = members
     }))
   }*/
  
  fun doCheck() {
    
    val afj = AsyncForkJoin()
    
    afj.fork {
      val result = try {
        val reply = gtfsLookupService.status(Empty.getDefaultInstance()).await()
        log.info("reply message: '$reply'")
        "healthy"
      } catch (ex: Throwable){
        log.warn("request failed", ex)
        "failure: $ex"
      }
      status.put(GtfsLookupModule.address, result)
    }
  
    afj.fork {
      val result = try {
        val reply = organizationService.status(Empty.getDefaultInstance()).await()
        log.info("reply message: '$reply'")
        "healthy"
      } catch (ex: Throwable){
        log.warn("request failed", ex)
        "failure: $ex"
      }
      status.put(OrganizationModule.address, result)
    }
  
    afj.fork {
      val result = try {
        val reply = status(Empty.getDefaultInstance()).await()
        log.info("reply message: '$reply'")
        "healthy"
      } catch (ex: Throwable){
        log.warn("request failed", ex)
        "failure: $ex"
      }
      status.put(OrganizationModule.address, result)
    }
  
    afj.join().onCompleted { ar ->
      vertx.setTimer(delay) { doCheck() }
    }
    
    val hzCluster = hz.cluster
    println("showing cluster members....")
    for (hzMember in hzCluster.members) {
      println("address: ${hzMember.address},  uuid: ${hzMember.uuid}, v${hzMember.version}")
    }
    println("showing hazelcast information....")
    for (distObj in hz.distributedObjects) {
      println("-------------------------- ${distObj.javaClass.canonicalName} --------------------------")
      println("- name: ${distObj.name}")
      println("- partition key: ${distObj.partitionKey}")
      println("- service name: ${distObj.serviceName}")
      if (distObj is IMap<*, *>) {
        println("- size: ${distObj.size}")
        distObj.forEach { (k, v) ->
          println("- [$k]: $v")
        }
      }
      if (distObj is MultiMap<*, *>) {
        println("- size: ${distObj.size()}")
        distObj.entrySet().forEach { (k, v) ->
          println("- [$k]: $v")
        }
      }
    }
    println("deployments: ${vertx.deploymentIDs()}")
  }
  
  inline fun <reified T> ArrayList<T>.addNew(configure: T.() -> Unit) {
    add(T::class.java.newInstance().apply(configure))
  }
  
  companion object {
    val INVALID_TIMER_ID = -1L
  }
}