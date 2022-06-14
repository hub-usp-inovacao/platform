package br.usp.inovacao.hubusp.catalog

import com.mongodb.client.MongoDatabase
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.util.pipeline.PipelineContext
import org.litote.kmongo.getCollection

@kotlinx.serialization.Serializable
data class Discipline(
    val name: String,
    val unity: String,
)

class DisciplineController(
    private val db: MongoDatabase
) {

    suspend fun search(context: PipelineContext<Unit, ApplicationCall>) {
        val disciplineCollection = db.getCollection<Discipline>()
        val disciplines = disciplineCollection.find().toSet()
        context.call.respond(mapOf("disciplines" to disciplines))
    }
}

fun Application.configureCatalogRouting(db: MongoDatabase) {
    val disciplineController = DisciplineController(db)

    routing {
        get("/disciplines") {
            disciplineController.search(this)
        }
    }
}
