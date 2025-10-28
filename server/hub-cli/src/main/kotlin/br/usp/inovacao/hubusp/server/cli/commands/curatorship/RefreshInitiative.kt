package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.InitiativeErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.InitiativeRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshInitiative : CliktCommand() {
    private val refreshInitiative: br.usp.inovacao.hubusp.curatorship.sheets.RefreshInitiative

    init {
        val db =
            configureDB(
                Configuration.database.protocol,
                Configuration.database.host,
                Configuration.database.port,
                Configuration.database.dbName,
            )

        refreshInitiative =
            br.usp.inovacao.hubusp.curatorship.sheets.RefreshInitiative(
                mailer =
                    Mailer(
                        Configuration.email.username,
                        Configuration.email.password,
                    ),
                spreadsheetReader = SpreadsheetReader(),
                initiativeRepository = InitiativeRepositoryImpl(db),
                initiativeErrorRepository = InitiativeErrorRepositoryImpl(db),
            )
    }

    override fun run() {
        refreshInitiative.refresh()
    }
}
