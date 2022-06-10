package br.usp.inovacao.hubusp.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        get("/disciplines") {
            call.respondText("In the future: disciplines")
        }
    }
}
