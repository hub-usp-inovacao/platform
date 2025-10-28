package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.config.Configuration
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.cors.routing.CORS

@Suppress("unused")
fun Application.configureHttp() {
    val anyHostSymbol = "*"

    val corsHosts = Configuration.ktor.allowedHosts

    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Post)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)

        if (corsHosts.contains(anyHostSymbol)) anyHost()
        else corsHosts.forEach { allowHost(it, schemes = listOf("http", "https")) }
    }
}
