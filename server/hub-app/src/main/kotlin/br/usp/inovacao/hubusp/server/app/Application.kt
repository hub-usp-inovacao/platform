package br.usp.inovacao.hubusp.server.app

// TODO: Rename modules -> routes (or something similar/better)
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.server.app.modules.catalog
import br.usp.inovacao.hubusp.server.app.modules.configureCompaniesRoute
import br.usp.inovacao.hubusp.server.app.modules.discovery
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

    // TODO: Give these modules better names (configurePathRoute)
    catalog(db)
    discovery(db)

    configureCompaniesRoute(
        Mailer(
            environment.config.property("email.username").getString(),
            environment.config.property("email.password").getString(),
        ),
        listOf(environment.config.property("email.devs").getString()),
    )
}
