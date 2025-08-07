package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.curatorship.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.ResearcherErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.ResearcherRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshResearcher : CliktCommand() {
    private val refreshResearcher : br.usp.inovacao.hubusp.curatorship.sheets.RefreshResearcher

    init {
        val user = Configuration.EMAIL_USERNAME
        val password = Configuration.EMAIL_PASSWORD

        val protocol = Configuration.DATASOURCE_PROTOCOL
        val host = Configuration.DATASOURCE_HOST
        val port = Configuration.DATASOURCE_PORT
        val dbName = Configuration.DATASOURCE_DBNAME

        val db = configureDB(protocol, host, port, dbName)
        refreshResearcher = br.usp.inovacao.hubusp.curatorship.sheets.RefreshResearcher(
            mailer = Mailer(user, password),
            spreadsheetReader = SpreadsheetReader(),
            researcherRepository = ResearcherRepositoryImpl(db),
            researcherErrorRepository = ResearcherErrorRepositoryImpl(db)
        )
    }

    override fun run() {
        refreshResearcher.refresh()
    }
}