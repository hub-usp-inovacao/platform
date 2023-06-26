package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.server.app.modules.catalog
import br.usp.inovacao.hubusp.server.app.modules.discovery
import br.usp.inovacao.hubusp.configuration.Configuration
import br.usp.inovacao.hubusp.server.persistence.configureDB
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureHttp()
    configureSecurity()
    configureSerialization()
    configureRouting()
    configureCallLogging()

    val db = configureDB(
        protocol = Configuration.Persistence.PROTOCOL,
        host = Configuration.Persistence.HOST,
        port = Configuration.Persistence.PORT,
        dbName = Configuration.Persistence.DB_NAME
    )

    catalog(db)
    discovery(db)
}
