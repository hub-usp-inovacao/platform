package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.server.app.routing.configureCatalogRoute
import br.usp.inovacao.hubusp.server.app.routing.configureCompaniesRoute
import br.usp.inovacao.hubusp.server.app.routing.configureJourneyRoute
import br.usp.inovacao.hubusp.server.persistence.configureDB
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureHttp()
    configureSecurity()
    configureSerialization()
    configureRouting() // TODO: Remove this (unecessary routes)
    configureCallLogging()

    val db =
        configureDB(
            protocol = environment.config.property("datasource.protocol").getString(),
            host = environment.config.property("datasource.host").getString(),
            port = environment.config.property("datasource.port").getString(),
            dbName = environment.config.property("datasource.dbName").getString(),
        )

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
}
