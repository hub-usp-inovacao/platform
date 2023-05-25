package br.usp.inovacao.hubusp.server.persistence.journey

import br.usp.inovacao.hubusp.configuration.Configuration
import br.usp.inovacao.hubusp.server.discovery.*
import br.usp.inovacao.hubusp.server.persistence.models.DisciplineModel
import br.usp.inovacao.hubusp.server.persistence.models.InitiativeModel
import br.usp.inovacao.hubusp.server.persistence.models.PDIModel
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.litote.kmongo.find
import org.litote.kmongo.getCollection

@Serializable
private data class ValueRange(
    val range: String,
    val majorDimension: String,
    val values: List<List<String>>
)

class JourneyRepositoryImpl(
    db: MongoDatabase
) : JourneyRepository {

    private val disciplineCollection: MongoCollection<DisciplineModel>
    private val initiativeCollection: MongoCollection<InitiativeModel>
    private val pdiCollection: MongoCollection<PDIModel>
    private val httpClient: HttpClient

    init {
        disciplineCollection = db.getCollection<DisciplineModel>("disciplines")
        initiativeCollection = db.getCollection<InitiativeModel>("iniciatives")
        pdiCollection = db.getCollection<PDIModel>("pdis")
        httpClient = HttpClient(CIO) {
            install(ContentNegotiation) {
                json()
            }
        }
    }

    override fun find(step: JourneyStep, filter: Filter) = when(step) {
        JourneyStep.Learn -> findLearn(filter as LearnStepFilters)
        JourneyStep.Practice -> findPractice(filter as PracticeStepFilter)
        JourneyStep.Create -> findCreate(filter as CreateStepFilter)
        JourneyStep.Improve -> findImprove(filter as ImproveStepFilter)
        JourneyStep.Fund -> findFund(filter as FundStepFilter)
    }

    private fun findLearn(filter: LearnStepFilters): Set<JourneyRecord> {
        return disciplineCollection
            .find(filter.toCollectionFilter())
            .map {
                JourneyRecord(
                    name = it.name,
                    url = it.url
                )
            }
            .toSet()
    }

    private fun findPractice(filter: PracticeStepFilter): Set<JourneyRecord> {
        return initiativeCollection
            .find(filter.toCollectionFilter())
            .map {
                JourneyRecord(
                    name = it.name,
                    url = it.url
                )
            }
            .toSet()
    }

    private fun matchesCreateFilter(filter: CreateStepFilter, row: List<String>): Boolean {
        if (filter.insideUSP == null) return true

        val insideUSP: Boolean = filter.insideUSP!!
        val bond = row[1]

        return (insideUSP && bond == "USP") || (!insideUSP && bond == "Outro")
    }

    private fun findCreate(filter: CreateStepFilter): Set<JourneyRecord> {
        val sheetId = Configuration.GoogleSheets.INCUBATORS.ID
        val tabName = Configuration.GoogleSheets.INCUBATORS.TAB_NAME

        return readSpreadsheet(sheetId, tabName)
            .filter { row -> this.matchesCreateFilter(filter, row) }
            .map {
                JourneyRecord(
                    name = it[2],
                    url = it[3]
                )
            }
            .toSet()
    }

    private fun findImprove(filter: ImproveStepFilter): Set<JourneyRecord> {
        return pdiCollection
            .find(filter.toCollectionFilter())
            .map {
                JourneyRecord(
                    name = it.name,
                    url = it.site
                )
            }
            .toSet()
    }

    private fun findFund(filter: FundStepFilter): Set<JourneyRecord> {
        val sheetId = Configuration.GoogleSheets.FUNDS.ID
        val tabName = Configuration.GoogleSheets.FUNDS.TAB_NAME

        return readSpreadsheet(sheetId, tabName)
            .filter { filter.type == null || it[0] == filter.type!! }
            .map {
                JourneyRecord(
                    name = it[1],
                    url = it[2]
                )
            }
            .toSet()
    }

    private fun readSpreadsheet(sheetId: String, tabName: String): List<List<String>> = try {
        val apiKey = Configuration.GoogleSheets.API_KEY
        runBlocking {
            httpClient
                .get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = "sheets.googleapis.com"
                        appendPathSegments("v4", "spreadsheets", sheetId, "values", "'$tabName'")
                        parameters.append("key", apiKey)
                    }
                }
                .bodyAsText()
                .let { Json.decodeFromString<ValueRange>(it) }
                .values
        }
    } catch (e: Exception) {
        emptyList()
    }
}

fun LearnStepFilters.toCollectionFilter(): String {
    val inner = mutableSetOf<String>()

    if (level != null) inner.add("level:\"$level\"")

    if (nature != null) inner.add("nature:\"$nature\"")

    return "{" +  inner.joinToString(",") + "}"
}

fun PracticeStepFilter.toCollectionFilter(): String {
    val inner = mutableSetOf<String>()

    if (category != null) inner.add("classification:\"$category\"")

    return "{" + inner.joinToString(",") + "}"
}

fun ImproveStepFilter.toCollectionFilter(): String {
    val inner = mutableSetOf<String>()

    if (category != null) inner.add("category:\"$category\"")

    return "{" + inner.joinToString(",") + "}"
}