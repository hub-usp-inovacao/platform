package br.usp.inovacao.hubusp.server.cli.commands.curatorship

import br.usp.inovacao.hubusp.curatorship.Mailer
import br.usp.inovacao.hubusp.curatorship.sheets.SpreadsheetReader
import br.usp.inovacao.hubusp.server.persistence.configureDB
import br.usp.inovacao.hubusp.server.persistence.curatorship.PDIErrorRepositoryImpl
import br.usp.inovacao.hubusp.server.persistence.curatorship.PDIRepositoryImpl
import com.github.ajalt.clikt.core.CliktCommand

class RefreshPDI : CliktCommand() {
    private val refreshPDI : br.usp.inovacao.hubusp.curatorship.sheets.RefreshPDI

    init {
        // TODO: replace empty strings with configuration
        val db = configureDB("", "", "", "")
        refreshPDI = br.usp.inovacao.hubusp.curatorship.sheets.RefreshPDI(
            mailer = Mailer("", ""),
            spreadsheetReader = SpreadsheetReader(""),
            pdiRepository = PDIRepositoryImpl(db),
            pdiErrorRepository = PDIErrorRepositoryImpl(db)
        )
    }

    override fun run() {
        refreshPDI.refresh()
    }
}