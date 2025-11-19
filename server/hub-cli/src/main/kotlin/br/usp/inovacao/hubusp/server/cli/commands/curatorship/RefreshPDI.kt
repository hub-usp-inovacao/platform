package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.curatorship.PDIErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PDIRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshPDI : CliktCommand() {
    private val refreshPDI: br.usp.inovacao.hubusp.curatorship.sheets.RefreshPDI by lazy {
        br.usp.inovacao.hubusp.curatorship.sheets.RefreshPDI(
            mailer =
                Mailer(
                    Configuration.email.username,
                    Configuration.email.password,
                ),
            spreadsheetReader = SpreadsheetReader(),
            pdiRepository = PDIRepositoryImpl(Refresh.db),
            pdiErrorRepository = PDIErrorRepositoryImpl(Refresh.db),
        )
    }

    override fun run() {
        refreshPDI.refresh()
    }
}
