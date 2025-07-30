package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.server.app.routing.configureCatalogRoute
import br.usp.inovacao.hubusp.server.app.routing.configureCompaniesRoute
import br.usp.inovacao.hubusp.server.app.routing.configureJourneyRoute
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(db: MongoDatabase) {
    // TODO: Prepend /catalog to avoid conflicts
    // (this would require updating Caddy to not strip /catalog)
    configureCatalogRoute(db)
    configureJourneyRoute(db)
    configureCompaniesRoute(
        Mailer(
            environment.config.property("email.username").getString(),
            environment.config.property("email.password").getString(),
        ),
        listOf(environment.config.property("email.devs").getString()),
    )

    routing {
        get("/") { call.respond("app root ok") }
        authenticate { get("/admin/") { call.respond("admin root ok") } }
    }
}
