package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.server.app.modules.catalog
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
    configureRouting()
    configureCallLogging()

    val db = configureDB(
        protocol = environment.config.property("datasource.protocol").getString(),
        host = environment.config.property("datasource.host").getString(),
        port = environment.config.property("datasource.port").getString(),
        dbName = environment.config.property("datasource.dbName").getString()
    )

    catalog(db)
    discovery(db)
}
