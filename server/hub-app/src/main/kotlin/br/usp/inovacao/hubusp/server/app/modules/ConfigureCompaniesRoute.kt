package br.usp.inovacao.hubusp.server.app.modules

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureCompaniesRoute() {
    routing {
        route("/companies") {
            post("/company") {
                // TODO: Get data from POST request
                // TODO: Validation, return constraint validation errors
                call.respond(HttpStatusCode.OK)
            }
        }
    }
}
