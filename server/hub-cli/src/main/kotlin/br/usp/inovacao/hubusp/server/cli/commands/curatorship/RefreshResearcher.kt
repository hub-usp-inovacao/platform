package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.curatorship.ResearcherErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.ResearcherRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshResearcher : CliktCommand() {
    private val refreshResearcher:
        br.usp.inovacao.hubusp.curatorship.sheets.RefreshResearcher by lazy {
        br.usp.inovacao.hubusp.curatorship.sheets.RefreshResearcher(
            mailer =
                Mailer(
                    Configuration.email.username,
                    Configuration.email.password,
                ),
            spreadsheetReader = SpreadsheetReader(),
            researcherRepository = ResearcherRepositoryImpl(Refresh.db),
            researcherErrorRepository = ResearcherErrorRepositoryImpl(Refresh.db),
        )
    }

    override fun run() {
        refreshResearcher.refresh()
    }
}
