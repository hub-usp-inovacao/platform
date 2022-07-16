package br.usp.inovacao.hubusp.server.app

import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting() {
    routing {
        get("/") {
            call.respond("app root ok")
        }

        authenticate {
            get("/admin/") {
                call.respond("admin root ok")
            }
        }
    }
}
