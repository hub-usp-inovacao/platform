package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.CompanyErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.CompanyRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshCompany : CliktCommand() {
    private val refreshCompany: br.usp.inovacao.hubusp.curatorship.sheets.RefreshCompany

    init {
        val db =
            configureDB(
                Configuration.database.protocol,
                Configuration.database.host,
                Configuration.database.port,
                Configuration.database.dbName,
            )

        refreshCompany =
            br.usp.inovacao.hubusp.curatorship.sheets.RefreshCompany(
                mailer =
                    Mailer(
                        Configuration.email.username,
                        Configuration.email.password,
                    ),
                spreadsheetReader = SpreadsheetReader(),
                companyRepository = CompanyRepositoryImpl(db),
                companyErrorRepository = CompanyErrorRepositoryImpl(db),
            )
    }

    override fun run() {
        refreshCompany.refresh()
    }
}
