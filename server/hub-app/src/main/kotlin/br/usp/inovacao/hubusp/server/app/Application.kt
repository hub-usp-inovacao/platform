package br.usp.inovacao.hubusp.server.app

import br.usp.inovacao.hubusp.server.app.modules.catalog
import br.usp.inovacao.hubusp.server.configureHttp
import br.usp.inovacao.hubusp.server.configureRouting
import br.usp.inovacao.hubusp.server.configureSecurity
import br.usp.inovacao.hubusp.server.configureSerialization
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

@Suppress("unused")
fun Application.module() {
    configureHttp()
    configureSecurity()
    configureSerialization()
    configureRouting()

    catalog()
}
