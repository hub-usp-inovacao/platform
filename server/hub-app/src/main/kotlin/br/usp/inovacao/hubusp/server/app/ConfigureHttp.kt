package br.usp.inovacao.hubusp.server.app

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

@Suppress("unused")
fun Application.configureHttp() {
    val anyHostSymbol = "*"

    val corsHosts = environment.config.property("ktor.deployment.allowedHosts").getString()

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")

        if (corsHosts == anyHostSymbol) anyHost()
        else corsHosts
            .split(";")
            .forEach { allowHost(it, schemes = listOf("http", "https")) }
    }
}
