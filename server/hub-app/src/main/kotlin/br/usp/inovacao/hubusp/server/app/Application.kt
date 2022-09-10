package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.server.app.modules.catalog
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
    configureMetrics()

    catalog()
}
