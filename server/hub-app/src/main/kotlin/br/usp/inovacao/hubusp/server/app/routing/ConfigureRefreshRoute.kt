package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshCompany
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshDiscipline
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshInitiative
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshPDI
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshPatent
import br.usp.inovacao.hubusp.curatorship.sheets.RefreshResearcher
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.curatorship.CompanyErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.CompanyRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.DisciplineErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.DisciplineRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.InitiativeErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.InitiativeRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PDIErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PDIRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PatentErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PatentRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.ResearcherErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.ResearcherRepositoryImpl
import com.mongodb.client.MongoDatabase
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun Application.configureRefreshRoute(db: MongoDatabase) {
    val app = this
    val mailer by lazy {
        Mailer(Configuration.email.username, Configuration.email.password)
    }
    val spreadsheetReader by lazy { SpreadsheetReader() }

    routing {
        post("/refresh") {
            app.launch(Dispatchers.IO) {
                app.log.info("Refresh: starting all entities")

                try {
                    RefreshCompany(
                        mailer, spreadsheetReader,
                        CompanyRepositoryImpl(db), CompanyErrorRepositoryImpl(db),
                    ).refresh()
                    app.log.info("Refresh: companies done")

                    RefreshPDI(
                        mailer, spreadsheetReader,
                        PDIRepositoryImpl(db), PDIErrorRepositoryImpl(db),
                    ).refresh()
                    app.log.info("Refresh: PDIs done")

                    RefreshDiscipline(
                        mailer, spreadsheetReader,
                        DisciplineRepositoryImpl(db), DisciplineErrorRepositoryImpl(db),
                    ).refresh()
                    app.log.info("Refresh: disciplines done")

                    RefreshResearcher(
                        mailer, spreadsheetReader,
                        ResearcherRepositoryImpl(db), ResearcherErrorRepositoryImpl(db),
                    ).refresh()
                    app.log.info("Refresh: researchers done")

                    RefreshInitiative(
                        mailer, spreadsheetReader,
                        InitiativeRepositoryImpl(db), InitiativeErrorRepositoryImpl(db),
                    ).refresh()
                    app.log.info("Refresh: initiatives done")

                    RefreshPatent(
                        mailer, spreadsheetReader,
                        PatentRepositoryImpl(db), PatentErrorRepositoryImpl(db),
                    ).refresh()
                    app.log.info("Refresh: patents done")

                    app.log.info("Refresh: all entities completed successfully")
                } catch (e: Exception) {
                    app.log.error("Refresh: failed with exception", e)
                }
            }

            call.respond(HttpStatusCode.Accepted, mapOf("message" to "Refresh started"))
        }
    }
}
