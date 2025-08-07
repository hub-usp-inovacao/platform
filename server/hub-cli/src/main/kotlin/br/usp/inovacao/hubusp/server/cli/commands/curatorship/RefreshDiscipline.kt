package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.curatorship.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.DisciplineErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.DisciplineRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshDiscipline : CliktCommand() {
    private val refreshDiscipline : br.usp.inovacao.hubusp.curatorship.sheets.RefreshDiscipline

    init {
        val user = Configuration.EMAIL_USERNAME
        val password = Configuration.EMAIL_PASSWORD

        val protocol = Configuration.DATASOURCE_PROTOCOL
        val host = Configuration.DATASOURCE_HOST
        val port = Configuration.DATASOURCE_PORT
        val dbName = Configuration.DATASOURCE_DBNAME

        val db = configureDB(protocol, host, port, dbName)
        refreshDiscipline = br.usp.inovacao.hubusp.curatorship.sheets.RefreshDiscipline(
            mailer = Mailer(user, password),
            spreadsheetReader = SpreadsheetReader(),
            disciplineRepository = DisciplineRepositoryImpl(db),
            disciplineErrorRepository = DisciplineErrorRepositoryImpl(db)
        )
    }

    override fun run() {
        refreshDiscipline.refresh()
    }
}