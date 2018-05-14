package org.andreyko.vertx.protobuf.services.api.gateway

import com.google.protobuf.*
import com.satori.libs.async.api.*
import com.satori.libs.async.kotlin.*
import com.satori.libs.vertx.kotlin.*
import org.andreyko.vertx.protobuf.services.gtfs.lookup.service.*
import org.andreyko.vertx.protobuf.services.health.check.service.*
import org.andreyko.vertx.protobuf.services.organization.service.*
import javax.inject.*

class ApiGateway @Inject constructor(
  val healthCheckService: IHealthCheck,
  val gtfsLookupService: IGtfsLookup,
  val organizationService: IOrganization
) : IApiGateway {
  
  fun start() {
  
  }
  
  fun stop() {
  }
  
  override fun getAgencies(): IAsyncFuture<MutableList<Agency>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun getAgencyById(agencyId: String?): IAsyncFuture<Agency> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun getAgenciesByOrgId(orgId: String?): IAsyncFuture<MutableList<Agency>> {
    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
  }
  
  override fun healthCheck() = future {
    val serviceStatuses = healthCheckService.getServiceStatuses(Empty.getDefaultInstance()).await()
    return@future serviceStatuses.serviceStatusList.map {
      HealthStatus().apply {
        service = it.name
        status = it.status
      }
    }
  }
}