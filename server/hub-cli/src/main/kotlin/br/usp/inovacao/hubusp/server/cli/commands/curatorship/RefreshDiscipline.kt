package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.DisciplineErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.DisciplineRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshDiscipline : CliktCommand() {
    private val refreshDiscipline: br.usp.inovacao.hubusp.curatorship.sheets.RefreshDiscipline

    init {
        val db =
            configureDB(
                Configuration.database.protocol,
                Configuration.database.host,
                Configuration.database.port,
                Configuration.database.dbName,
            )

        refreshDiscipline =
            br.usp.inovacao.hubusp.curatorship.sheets.RefreshDiscipline(
                mailer =
                    Mailer(
                        Configuration.email.username,
                        Configuration.email.password,
                    ),
                spreadsheetReader = SpreadsheetReader(),
                disciplineRepository = DisciplineRepositoryImpl(db),
                disciplineErrorRepository = DisciplineErrorRepositoryImpl(db),
            )
    }

    override fun run() {
        refreshDiscipline.refresh()
    }
}
