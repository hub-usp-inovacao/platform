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

    // TODO: Fix this sh*t
    // - It rejects requests from Origins different from the server's
    // - It allowed POST even though it was not explicitely allowed
    // - It allows any Origin that ends with a slash (fixed in 3.0.0)
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")

        if (corsHosts == anyHostSymbol) anyHost()
        else corsHosts
            .split(";")
            .forEach { allowHost(it, schemes = listOf("http", "https")) }
    }
}
