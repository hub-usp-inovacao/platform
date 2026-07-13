package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.sheets.SpreadsheetWriter
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing

fun Application.configureRouting(db: MongoDatabase) {
    val mailer =
        Mailer(
            Configuration.email.username,
            Configuration.email.password,
        )

    // TODO: Prepend /catalog to avoid conflicts
    // (this would require updating Caddy to not strip /catalog)
    configureCatalogRoute(db)
    configureJourneyRoute(db)
    configureRefreshRoute(db)
    configureCompanyRoute(
        mailer,
        Configuration.email.devs,
        SpreadsheetWriter(
            Configuration.sheets.companyRegisterForm.id,
            Configuration.sheets.companyRegisterForm.tab,
        ),
        db,
    )
    configureCompatibilityRoute(db, mailer)

    routing {
        get("/") { call.respond("app root ok") }
        authenticate { get("/admin/") { call.respond("admin root ok") } }
    }
}
