package br.usp.inovacao.hubusp.server.app

import io.ktor.server.application.*
import io.ktor.server.metrics.micrometer.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureMetrics() {
    val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
    install(MicrometerMetrics) {
        registry = appMicrometerRegistry
    }

    routing {
        get("/metrics") {
            call.respondText(appMicrometerRegistry.scrape())
        }
    }
}