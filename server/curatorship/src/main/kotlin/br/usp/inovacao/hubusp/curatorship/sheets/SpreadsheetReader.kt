package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Configuration
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.SocketException

enum class Sheets {
    PDIs, Disciplines, Companies,
    Patents, Researchers, Initiatives
}

typealias Matrix<T> = List<List<T>>

@Serializable
private data class ValueRange(
    val range: String,
    val majorDimension: String,
    val values: Matrix<String>
)

class SpreadsheetReader(
    private val apiKey: String,
) {
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    @Suppress("unused")
    fun fakeRead(sheet: Sheets): Matrix<String> = emptyList()

    fun read(sheet: Sheets): Matrix<String> = when(sheet) {
        Sheets.PDIs -> readPDI()
        Sheets.Companies -> readCompany()
        Sheets.Disciplines -> readDiscipline()
        Sheets.Patents -> readPatent()
        Sheets.Researchers -> readResearcher()
        Sheets.Initiatives -> readInitiative()
    }

    private fun readPDI(): Matrix<String> = readOneSpreadsheet(
        sheetId = Configuration.PDI_SHEET_ID,
        sheetName = Configuration.PDI_TAB_NAME
    )

    private fun readCompany(): Matrix<String> = readOneSpreadsheet(
        sheetId = Configuration.COMPANY_SHEET_ID,
        sheetName = Configuration.COMPANY_TAB_NAME
    )

    private fun readDiscipline(): Matrix<String> = readOneSpreadsheet(
        sheetId = Configuration.DISCIPLINE_SHEET_ID,
        sheetName = Configuration.DISCIPLINE_TAB_NAME
    )

    private fun readPatent(): Matrix<String> = readOneSpreadsheet(
        sheetId = Configuration.PATENT_SHEET_ID,
        sheetName = Configuration.PATENT_TAB_NAME
    )

    private fun readResearcher(): Matrix<String> = readOneSpreadsheet(
        sheetId = Configuration.RESEARCHER_SHEET_ID,
        sheetName = Configuration.RESEARCHER_TAB_NAME
    )

    private fun readInitiative(): Matrix<String> = readOneSpreadsheet(
        sheetId = Configuration.INITIATIVE_SHEET_ID,
        sheetName = Configuration.INITIATIVE_TAB_NAME
    )

    private fun readOneSpreadsheet(sheetId: String, sheetName: String): Matrix<String> = try {
        val values = runBlocking {
            httpClient
                .get {
                    url {
                        protocol = URLProtocol.HTTPS
                        host = "sheets.googleapis.com"
                        appendPathSegments("v4", "spreadsheets", sheetId, "values", "'$sheetName'")
                        parameters.append("key", apiKey)
                    }
                }
                .bodyAsText()
                .let { Json.decodeFromString<ValueRange>(it) }
                .values
        }

        if (values.isEmpty()) throw SheetReadingException(sheetName, sheetId, message = "Spreadsheet is empty")

        values
    } catch (_: SocketException) {
        throw SheetReadingException(sheetName, sheetId, message = "Failed to connect to spreadsheet")
    } catch (_: SerializationException) {
        throw SheetReadingException(sheetName, sheetId, message = "Unexpected response from spreadsheet")
    }
}
