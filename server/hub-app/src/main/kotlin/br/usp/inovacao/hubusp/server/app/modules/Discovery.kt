package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.server.discovery.*
import br.usp.inovacao.hubusp.server.persistence.journey.JourneyRepositoryImpl
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Parameters.toLearnStepParams() = LearnStepFilters(
    nature = this["nature"],
    level = this["level"]
)

fun Parameters.toPracticeStepParams() = PracticeStepFilter(
    category = this["category"]
)

fun Parameters.toCreateStepParams() = CreateStepFilter(
    insideUSP = this["insideUSP"] == "true"
)

fun Parameters.toImproveStepParams() = ImproveStepFilter(
    category = this["category"]
)

fun Parameters.toFundStepParams() = FundStepFilter(
    type = this["type"]
)

fun Application.discovery(db: MongoDatabase) {
    val journey = JourneyRepositoryImpl(db)
        .let { Journey(it) }

    routing {
        route("/journey") {
            get("/learn") {
                val params = call.request.queryParameters
                val records = journey.learnStep(params.toLearnStepParams())
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("records" to records)
                )
            }

            get("/practice") {
                val params = call.request.queryParameters
                val records = journey.practiceStep(params.toPracticeStepParams())
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("records" to records)
                )
            }

            get("/create") {
                val params = call.request.queryParameters
                val records = journey.createStep(params.toCreateStepParams())
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("records" to records)
                )
            }

            get("/improve") {
                val params = call.request.queryParameters
                val records = journey.improveStep(params.toImproveStepParams())
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("records" to records)
                )
            }

            get("/fund") {
                val params = call.request.queryParameters
                val records = journey.fundStep(params.toFundStepParams())
                call.respond(
                    HttpStatusCode.OK,
                    mapOf("records" to records)
                )
            }
        }
    }
}