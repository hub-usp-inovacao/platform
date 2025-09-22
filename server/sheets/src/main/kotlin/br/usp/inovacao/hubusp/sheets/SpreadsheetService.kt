package br.usp.inovacao.hubusp.sheets

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets as GoogleSheetsService
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import java.io.IOException

typealias Matrix<T> = List<List<T>>

/**
 * Singleton wrapper around the google sheets API
 *
 * https://developers.google.com/workspace/sheets/api/guides/values
 */
object SpreadsheetService {
    private const val CREDENTIALS_FILE_PATH = "/credentials.json"
    private const val APPLICATION_NAME = "HubUSP"

    val googleSheetsService: GoogleSheetsService by lazy {
        try {
            val credentialsStream =
                this::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
                    ?: throw IOException("Credentials file not found at $CREDENTIALS_FILE_PATH")

            val credentials =
                GoogleCredentials.fromStream(credentialsStream)
                    .createScoped(listOf(SheetsScopes.SPREADSHEETS_READONLY))

            GoogleSheetsService.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    GsonFactory.getDefaultInstance(),
                    HttpCredentialsAdapter(credentials),
                )
                .setApplicationName(APPLICATION_NAME)
                .build()
        } catch (e: Exception) {
            throw RuntimeException("Failed to initialize Google Sheets service", e)
        }
    }

    /** Append `values` to `sheetId` at `range` */
    fun append(sheetId: String, range: String, values: Matrix<String?>) =
        googleSheetsService
            .spreadsheets()
            .values()
            .append(sheetId, range, ValueRange().setValues(values))
            .execute()
}
