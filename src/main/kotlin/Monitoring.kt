package com.homegrown

import com.fasterxml.jackson.databind.*
import dev.hayden.KHealth
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheus.*
import java.sql.Connection
import java.sql.DriverManager

fun Application.configureMonitoring() {
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    
    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
        // ...
    }
    install(KHealth)
    routing {
        get("/metrics-micrometer") {
            call.respond(appMicrometerRegistry.scrape())
        }
    }
}
