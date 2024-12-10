package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.server.catalog.*
import br.usp.inovacao.hubusp.server.persistence.*
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

fun String.toBooleanPTBR(): Boolean {
    val decoded = URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
    return when (decoded) {
        "sim", "Sim", "SIM", "s", "S" -> true
        "não", "Não", "NÃO", "n", "N", "ñ", "Ñ" -> false
        else -> throw IllegalArgumentException("Invalid boolean value in PT-BR: $decoded (decoded from $this)")
    }
}

fun Parameters.toPDISearchParams() = PDISearchParams(
    categories = this["categories"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    term = this["term"],
)

fun Parameters.toResearcherSearchParams() = ResearcherSearchParams(
    majorArea = this["areaMajors"]?.split(",")?.toSet() ?: emptySet(),
    minorArea = this["areaMinors"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    unity = this["unity"],
    bond = this["bond"],
    term = this["term"]
)

fun Parameters.toCompanySearchParams() = CompanySearchParams(
    areaMajors = this["areaMajors"]?.split(',')?.toSet() ?: emptySet(),
    areaMinors = this["areaMinors"]?.split(',')?.toSet() ?: emptySet(),
    unity = this["unity"],
    city = this["city"],
    ecosystem = this["ecosystem"],
    size = this["size"],
    term = this["term"]
)

fun Parameters.toPatentSearchParams() = PatentSearchParams(
    majorAreas = this["areaMajors"]?.split(",")?.toSet() ?: emptySet(),
    minorAreas = this["areaMinors"]?.split(",")?.toSet() ?: emptySet(),
    status = this["status"],
    term = this["term"],
)

fun Parameters.toInitiativeSearchParams() = InitiativeSearchParams(
    classifications = this["classifications"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    term = this["term"]
)

fun Parameters.toDisciplineSearchParams() = DisciplineSearchParams(
    categories = this["categories"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    unity = this["unity"],
    level = this["level"],
    nature = this["nature"],
    offeringPeriod = this["offeringPeriod"],
    beingOffered = this["beingOffered"]?.toBooleanPTBR(),
    term = this["term"],
)


fun Application.catalog(db: MongoDatabase) {
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
        get("/ecosystems") {
            val ecosystems = searchCompanies.getAllEcosystems()

            call.respond(
                HttpStatusCode.OK,
                mapOf("ecosystems" to ecosystems)
            )
        }

        get("/company_cities") {
            val cities = searchCompanies.getAllCities()

            call.respond(
                HttpStatusCode.OK,
                mapOf("cities" to cities)
            )
        }

        get("/patent_classifications") {
            val classifications = searchPatents.getAllClassifications()

            call.respond(
                HttpStatusCode.OK,
                mapOf("classifications" to classifications)
            )
        }

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
