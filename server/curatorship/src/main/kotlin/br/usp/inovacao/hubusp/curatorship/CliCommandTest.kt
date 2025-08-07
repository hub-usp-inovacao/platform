package br.usp.inovacao.hubusp.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Sheets
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader

class CliCommandTest {
    private val reader: SpreadsheetReader
    private val mailer: Mailer

    init {
        val user = Configuration.EMAIL_USERNAME
        val password = Configuration.EMAIL_PASSWORD

        reader = SpreadsheetReader()
        mailer = Mailer(user, password)
    }

    fun execute() {
        val data = reader.read(Sheets.PDIs)
        mailer.notifySpreadsheetError("Found ${data.size} PDI records")
    }
}