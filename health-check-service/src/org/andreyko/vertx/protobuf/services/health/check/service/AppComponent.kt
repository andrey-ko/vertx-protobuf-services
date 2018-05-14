package org.andreyko.vertx.protobuf.services.health.check.service

import dagger.*
import org.andreyko.vertx.protobuf.services.gtfs.lookup.service.*
import org.andreyko.vertx.protobuf.services.organization.service.*
import javax.inject.*

@Singleton
@Component(modules = arrayOf(
  AppModule::class,
  GtfsLookupModule::class,
  OrganizationModule::class
))
interface AppComponent {
  fun newHealthCheckVerticle(): HealthCheckVerticleExt
}
