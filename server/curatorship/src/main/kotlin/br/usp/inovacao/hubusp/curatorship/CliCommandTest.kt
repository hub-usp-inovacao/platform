package br.usp.inovacao.hubusp.curatorship

import br.usp.inovacao.hubusp.configuration.Configuration
import br.usp.inovacao.hubusp.curatorship.sheets.Sheets
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader

class CliCommandTest {
    private val reader: SpreadsheetReader
    private val mailer: Mailer

    init {
        val apiKey = Configuration.GoogleSheets.API_KEY
        val user = Configuration.Email.USERNAME
        val password = Configuration.Email.PASSWORD

        reader = SpreadsheetReader(apiKey)
        mailer = Mailer(user, password)
    }

    fun execute() {
        val data = reader.read(Sheets.PDIs)
        mailer.notifySpreadsheetError("Found ${data.size} PDI records")
    }
}