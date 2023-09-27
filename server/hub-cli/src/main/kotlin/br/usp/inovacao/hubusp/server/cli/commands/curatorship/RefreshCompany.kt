package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.curatorship.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.CompanyErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.CompanyRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshCompany : CliktCommand() {
    private val refreshCompany : br.usp.inovacao.hubusp.curatorship.sheets.RefreshCompany

    init {
        val user = Configuration.EMAIL_USERNAME
        val password = Configuration.EMAIL_PASSWORD
        val apiKey = Configuration.SHEETS_API_KEY

        val protocol = Configuration.DATASOURCE_PROTOCOL
        val host = Configuration.DATASOURCE_HOST
        val port = Configuration.DATASOURCE_PORT
        val dbName = Configuration.DATASOURCE_DBNAME

        val db = configureDB(protocol, host, port, dbName)
        refreshCompany = br.usp.inovacao.hubusp.curatorship.sheets.RefreshCompany(
            mailer = Mailer(user, password),
            spreadsheetReader = SpreadsheetReader(apiKey),
            companyRepository = CompanyRepositoryImpl(db),
            companyErrorRepository = CompanyErrorRepositoryImpl(db)
        )
    }

    override fun run() {
        refreshCompany.refresh()
    }
}
