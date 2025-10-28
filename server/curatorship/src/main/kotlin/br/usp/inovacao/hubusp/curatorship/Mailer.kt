package br.usp.inovacao.hubusp.curatorship

import br.usp.inovacao.hubusp.mailer.Mail

class Mailer(
    user: String,
    password: String
) {
    private val mailerImpl: br.usp.inovacao.hubusp.mailer.Mailer

    init {
        mailerImpl = br.usp.inovacao.hubusp.mailer.Mailer(user, password)
    }

    fun notifySpreadsheetError(message: String, subject: String = "Erro ao buscar as planilhas") {
        val developersEmail = Configuration.DEVS_EMAIL
        mailerImpl.send(
            Mail(
                to = setOf(developersEmail),
                subject = subject,
                body = message
            )
        )
    }
}
