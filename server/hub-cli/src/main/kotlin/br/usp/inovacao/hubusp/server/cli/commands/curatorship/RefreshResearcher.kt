package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.ResearcherErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.ResearcherRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshResearcher : CliktCommand() {
    private val refreshResearcher: br.usp.inovacao.hubusp.curatorship.sheets.RefreshResearcher

    init {
        val db =
            configureDB(
                Configuration.database.protocol,
                Configuration.database.host,
                Configuration.database.port,
                Configuration.database.dbName,
            )

        refreshResearcher =
            br.usp.inovacao.hubusp.curatorship.sheets.RefreshResearcher(
                mailer =
                    Mailer(
                        Configuration.email.username,
                        Configuration.email.password,
                    ),
                spreadsheetReader = SpreadsheetReader(),
                researcherRepository = ResearcherRepositoryImpl(db),
                researcherErrorRepository = ResearcherErrorRepositoryImpl(db),
            )
    }

    override fun run() {
        refreshResearcher.refresh()
    }
}
