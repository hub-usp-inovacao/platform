package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.server.app.routing.configureRouting
import br.usp.inovacao.hubusp.server.app.auth.configureAuthentication
import br.usp.inovacao.hubusp.server.persistence.configureDB
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureHttp()
    configureAuthentication()
    configureSerialization()
    configureRouting(
        configureDB(
            protocol = Configuration.database.protocol,
            host = Configuration.database.host,
            port = Configuration.database.port,
            dbName = Configuration.database.dbName,
        ),
    )
    configureCallLogging()
}
