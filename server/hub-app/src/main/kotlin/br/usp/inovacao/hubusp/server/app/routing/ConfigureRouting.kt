package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.sheets.SpreadsheetWriter
import com.mongodb.client.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.routing.get

fun Application.configureRouting(db: MongoDatabase) {
    // TODO: Prepend /catalog to avoid conflicts
    // (this would require updating Caddy to not strip /catalog)
    configureCatalogRoute(db)
    configureJourneyRoute(db)
    configureCompanyRoute(
        db,
        Mailer(
            Configuration.email.username,
            Configuration.email.password,
        ),
        SpreadsheetWriter(
            Configuration.sheets.companyRegisterForm.id,
            Configuration.sheets.companyRegisterForm.tab,
        ),
    )
}
