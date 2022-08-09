package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.server.catalog.*
import br.usp.inovacao.hubusp.server.persistence.*
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
        .let { SearchDisciplines(it) }

    val searchResearchers = CatalogResearcherRepositoryImpl(db)
        .let { SearchResearchers(it) }

    val searchCompanies = CatalogCompanyRepositoryImpl(db)
        .let { SearchCompanies(it) }

    val searchPatents = CatalogPatentRepositoryImpl(db)
        .let { SearchPatents(it) }

    val searchInitiatives = CatalogInitiativeRepositoryImpl(db)
        .let { SearchInitiatives(it) }

    val repo = CatalogPDIRepositoryImpl(db)
    val searchPDIs = SearchPDIs(repo)

    routing {
        get("/pdis") {
            val params = call.request.queryParameters

            val pdis = searchPDIs.search(params.toPDISearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("pdis" to pdis)
            )
        }

        get("/disciplines") {
            val params = call.request.queryParameters.toDisciplineSearchParams()
            val disciplines = searchDisciplines.search(params)

            call.respond(
                HttpStatusCode.OK,
                mapOf("disciplines" to disciplines)
            )
        }

        get("/skills") {
            val params = call.request.queryParameters

            val researchers = searchResearchers.search(params.toResearcherSearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("skills" to researchers)
            )
        }

        get("/companies") {
            val params = call.request.queryParameters

            val companies = searchCompanies.search(params.toCompanySearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("companies" to companies)
            )
        }

        get("/patents") {
            val params = call.request.queryParameters

            val patents = searchPatents.search(params.toPatentSearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("patents" to patents)
            )
        }

        get("/initiatives") {
            val params = call.request.queryParameters

            val initiatives = searchInitiatives.search(params.toInitiativeSearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("initiatives" to initiatives)
            )
        }
    }
}
