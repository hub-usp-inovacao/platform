package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.configuration.Configuration
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

    fun read(sheet: Sheets): Matrix<String?> = when(sheet) {
        Sheets.PDIs -> readPDI()
        Sheets.Companies -> readCompany()
        Sheets.Disciplines -> readDiscipline()
        Sheets.Patents -> readPatent()
        Sheets.Researchers -> readResearcher()
        Sheets.Initiatives -> readInitiative()
    }

    private fun readPDI(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.GoogleSheets.PDI.ID,
        sheetName = Configuration.GoogleSheets.PDI.TAB_NAME
    )

    private fun readCompany(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.GoogleSheets.COMPANY.ID,
        sheetName = Configuration.GoogleSheets.COMPANY.TAB_NAME
    )

    private fun readDiscipline(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.GoogleSheets.DISCIPLINE.ID,
        sheetName = Configuration.GoogleSheets.DISCIPLINE.TAB_NAME
    )

    private fun readPatent(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.GoogleSheets.PATENT.ID,
        sheetName = Configuration.GoogleSheets.PATENT.TAB_NAME
    )

    private fun readResearcher(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.GoogleSheets.RESEARCHER.ID,
        sheetName = Configuration.GoogleSheets.RESEARCHER.TAB_NAME
    )

    private fun readInitiative(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.GoogleSheets.INITIATIVE.ID,
        sheetName = Configuration.GoogleSheets.INITIATIVE.TAB_NAME
    )

    private fun readOneSpreadsheet(sheetId: String, sheetName: String): Matrix<String?> = try {
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
                .map { row ->
                    row.map { cell ->
                        if (cell.isBlankCell()) null else cell
                    }
                }
        }

        if (values.isEmpty()) throw SheetReadingException(sheetName, sheetId, message = "Spreadsheet is empty")

        values
    } catch (_: SocketException) {
        throw SheetReadingException(sheetName, sheetId, message = "Failed to connect to spreadsheet")
    } catch (_: SerializationException) {
        throw SheetReadingException(sheetName, sheetId, message = "Unexpected response from spreadsheet")
    }
}

private fun String.isBlankCell(): Boolean {
    return isBlank() || equals("N/D")
}
