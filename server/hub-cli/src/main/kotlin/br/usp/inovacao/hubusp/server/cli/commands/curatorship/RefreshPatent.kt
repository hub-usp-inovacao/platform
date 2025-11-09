package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.PatentErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PatentRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshPatent : CliktCommand() {
    private val refreshPatent: br.usp.inovacao.hubusp.curatorship.sheets.RefreshPatent

    init {
        val db =
            configureDB(
                Configuration.database.protocol,
                Configuration.database.host,
                Configuration.database.port,
                Configuration.database.dbName,
            )

        refreshPatent =
            br.usp.inovacao.hubusp.curatorship.sheets.RefreshPatent(
                mailer =
                Mailer(
                    Configuration.email.username,
                    Configuration.email.password,
                ),
                spreadsheetReader = SpreadsheetReader(),
                patentRepository = PatentRepositoryImpl(db),
                patentErrorRepository = PatentErrorRepositoryImpl(db),
            )
    }

    override fun run() {
        refreshPatent.refresh()
    }
}
