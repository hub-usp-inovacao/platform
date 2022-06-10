package br.usp.inovacao.hubusp

import br.usp.inovacao.hubusp.plugins.configureDatabase
import br.usp.inovacao.hubusp.plugins.configureHTTP
import br.usp.inovacao.hubusp.plugins.configureRouting
import br.usp.inovacao.hubusp.plugins.configureSerialization
import io.ktor.server.application.Application

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    configureDatabase()

    configureHTTP()
    configureSerialization()
    configureRouting()
}
