package br.usp.inovacao.hubusp.catalog.application

import br.usp.inovacao.hubusp.catalog.domain.DisciplineService
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import io.ktor.util.toMap

class DisciplineController(
    private val disciplineService: DisciplineService,
) {
    suspend fun search(context: PipelineContext<Unit, ApplicationCall>) {
        val params = context.call.request.queryParameters.toMap()

        val disciplines = disciplineService.search(params)

        context.call.respond(mapOf("disciplines" to disciplines))
    }
}

fun Application.configureCatalogRouting(disciplineService: DisciplineService) {
    val disciplineController = DisciplineController(disciplineService)

    routing {
        get("/disciplines") {
            disciplineController.search(this)
        }
    }
}
