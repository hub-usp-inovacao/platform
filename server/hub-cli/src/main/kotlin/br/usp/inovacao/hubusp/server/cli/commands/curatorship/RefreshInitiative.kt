package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.curatorship.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.InitiativeErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.InitiativeRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshInitiative : CliktCommand() {
    private val refreshInitiative : br.usp.inovacao.hubusp.curatorship.sheets.RefreshInitiative

    init {
        val user = Configuration.EMAIL_USERNAME
        val password = Configuration.EMAIL_PASSWORD

        val protocol = Configuration.DATASOURCE_PROTOCOL
        val host = Configuration.DATASOURCE_HOST
        val port = Configuration.DATASOURCE_PORT
        val dbName = Configuration.DATASOURCE_DBNAME

        val db = configureDB(protocol, host, port, dbName)
        refreshInitiative = br.usp.inovacao.hubusp.curatorship.sheets.RefreshInitiative(
            mailer = Mailer(user, password),
            spreadsheetReader = SpreadsheetReader(),
            initiativeRepository = InitiativeRepositoryImpl(db),
            initiativeErrorRepository = InitiativeErrorRepositoryImpl(db)
        )
    }

    override fun run() {
        refreshInitiative.refresh()
    }
}
