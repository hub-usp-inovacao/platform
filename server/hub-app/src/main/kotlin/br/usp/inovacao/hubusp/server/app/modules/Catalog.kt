package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.server.catalog.DisciplineService
import br.usp.inovacao.hubusp.server.persistence.CatalogDisciplineRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.configureDB
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.toMap

fun Application.catalog() {
    val db = configureDB(
        protocol = environment.config.property("datasource.protocol").getString(),
        host = environment.config.property("datasource.host").getString(),
        port = environment.config.property("datasource.port").getString(),
        dbName = environment.config.property("datasource.dbName").getString()
    )

    val searchDisciplines = CatalogDisciplineRepositoryImpl(db)
        .let { DisciplineService(it) }

    routing {
        get("/disciplines") {
            val params = call.request.queryParameters.toMap()
            val disciplines = searchDisciplines.search(params)

            call.respond(
                HttpStatusCode.OK,
                mapOf("disciplines" to disciplines)
            )
        }
    }
}
