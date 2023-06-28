package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.curatorship.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.PDIErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PDIRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshPDI : CliktCommand() {
    private val refreshPDI : br.usp.inovacao.hubusp.curatorship.sheets.RefreshPDI

    init {
        val user = Configuration.EMAIL_USERNAME
        val password = Configuration.EMAIL_PASSWORD
        val apiKey = Configuration.SHEETS_API_KEY

        val protocol = Configuration.DATASOURCE_PROTOCOL
        val host = Configuration.DATASOURCE_HOST
        val port = Configuration.DATASOURCE_PORT
        val dbName = Configuration.DATASOURCE_DBNAME

        val db = configureDB(protocol, host, port, dbName)
        refreshPDI = br.usp.inovacao.hubusp.curatorship.sheets.RefreshPDI(
            mailer = Mailer(user, password),
            spreadsheetReader = SpreadsheetReader(apiKey),
            pdiRepository = PDIRepositoryImpl(db),
            pdiErrorRepository = PDIErrorRepositoryImpl(db)
        )
    }

    override fun run() {
        refreshPDI.refresh()
    }
}