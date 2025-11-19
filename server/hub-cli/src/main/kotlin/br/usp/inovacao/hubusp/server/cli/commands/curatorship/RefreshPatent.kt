package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.curatorship.PatentErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PatentRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshPatent : CliktCommand() {
    private val refreshPatent: br.usp.inovacao.hubusp.curatorship.sheets.RefreshPatent by lazy {
        br.usp.inovacao.hubusp.curatorship.sheets.RefreshPatent(
            mailer =
                Mailer(
                    Configuration.email.username,
                    Configuration.email.password,
                ),
            spreadsheetReader = SpreadsheetReader(),
            patentRepository = PatentRepositoryImpl(Refresh.db),
            patentErrorRepository = PatentErrorRepositoryImpl(Refresh.db),
        )
    }

    override fun run() {
        refreshPatent.refresh()
    }
}
