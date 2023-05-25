package br.usp.inovacao.hubusp.curatorship

import br.usp.inovacao.hubusp.configuration.Configuration
import br.usp.inovacao.hubusp.mailer.Mail

class Mailer(
    user: String,
    password: String
) {
    private val mailerImpl: br.usp.inovacao.hubusp.mailer.Mailer

    init {
        mailerImpl = br.usp.inovacao.hubusp.mailer.Mailer(user, password)
    }

    fun notifySpreadsheetError(message: String) {
        val developersEmail = Configuration.Email.DEVELOPERS
        mailerImpl.send(
            Mail(
                to = listOf(developersEmail),
                subject ="Erro ao buscar as planilhas",
                body = message
            )
        )
    }
}
