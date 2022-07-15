package br.usp.inovacao.hubusp.server

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

@Suppress("unused")
fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}
