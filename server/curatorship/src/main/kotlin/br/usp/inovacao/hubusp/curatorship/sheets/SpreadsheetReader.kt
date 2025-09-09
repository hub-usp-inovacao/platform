package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Configuration
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets as GoogleSheetsService
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException

enum class Sheets {
    PDIs, Disciplines, Companies,
    Patents, Researchers, Initiatives
}

typealias Matrix<T> = List<List<T>>

class SpreadsheetReader {
    private val sheetsService: GoogleSheetsService

    companion object {
        private const val CREDENTIALS_FILE_PATH = "../credentials.json"
        private const val APPLICATION_NAME = "HubUSP"
    }

    init {
        sheetsService = try {
            // Tentar carregar credentials.json do diretório server
            val credentialsFile = java.io.File("../credentials.json")
            val credentialsStream = if (credentialsFile.exists()) {
                credentialsFile.inputStream()
            } else {
                // Fallback para resources se o arquivo não existir no diretório pai
                SpreadsheetReader::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
                    ?: throw IOException("Credentials file not found at $CREDENTIALS_FILE_PATH or ../credentials.json")
            }

            val credentials = GoogleCredentials.fromStream(credentialsStream)
                .createScoped(listOf(SheetsScopes.SPREADSHEETS_READONLY))

            GoogleSheetsService.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                HttpCredentialsAdapter(credentials)
            )
                .setApplicationName(APPLICATION_NAME)
                .build()
        } catch (e: Exception) {
            throw RuntimeException("Failed to initialize Google Sheets service: ${e.message}", e)
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
        sheetId = Configuration.PDI_SHEET_ID,
        sheetName = Configuration.PDI_TAB_NAME
    )

    private fun readCompany(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.COMPANY_SHEET_ID,
        sheetName = Configuration.COMPANY_TAB_NAME
    )

    private fun readDiscipline(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.DISCIPLINE_SHEET_ID,
        sheetName = Configuration.DISCIPLINE_TAB_NAME
    )

    private fun readPatent(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.PATENT_SHEET_ID,
        sheetName = Configuration.PATENT_TAB_NAME
    )

    private fun readResearcher(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.RESEARCHER_SHEET_ID,
        sheetName = Configuration.RESEARCHER_TAB_NAME
    )

    private fun readInitiative(): Matrix<String?> = readOneSpreadsheet(
        sheetId = Configuration.INITIATIVE_SHEET_ID,
        sheetName = Configuration.INITIATIVE_TAB_NAME
    )

    private fun readOneSpreadsheet(sheetId: String, sheetName: String): Matrix<String?> {
        try {
            val range = "'$sheetName'"

            val response = sheetsService.spreadsheets().values()
                .get(sheetId, range)
                .execute()

            val values = response.getValues() as? Matrix<String> ?: emptyList()

            if (values.isEmpty()) {
                throw SheetReadingException(sheetName, sheetId, message = "Spreadsheet is empty")
            }

            return values.map { row ->
                row.map { cell ->
                    if (cell.isBlankCell()) null else cell
                }
            }
        } catch (_: IOException) {
            throw SheetReadingException(sheetName, sheetId, message = "Failed to connect to spreadsheet")
        } catch (_: Exception) {
            throw SheetReadingException(sheetName, sheetId, message = "Unexpected response from spreadsheet")
        }
    }
}

private fun String.isBlankCell(): Boolean {
    return isBlank() || equals("N/D")
}
