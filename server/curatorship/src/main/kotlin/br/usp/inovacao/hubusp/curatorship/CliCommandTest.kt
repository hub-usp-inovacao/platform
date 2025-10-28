package br.usp.inovacao.hubusp.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.sheets.Sheets
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader

class CliCommandTest {
    private val reader: SpreadsheetReader
    private val mailer: Mailer

    init {
        reader = SpreadsheetReader()
        mailer = Mailer(Configuration.email.username, Configuration.email.password)
    }

    fun execute() {
        val data = reader.read(Sheets.PDIs)
        mailer.notifySpreadsheetError("Found ${data.size} PDI records")
    }
}
