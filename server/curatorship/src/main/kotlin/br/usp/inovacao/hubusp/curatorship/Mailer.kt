package br.usp.inovacao.hubusp.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.mailer.Mail

class Mailer(user: String, password: String) {
    private val mailerImpl: br.usp.inovacao.hubusp.mailer.Mailer

    init {
        mailerImpl = br.usp.inovacao.hubusp.mailer.Mailer(user, password)
    }

    fun notifySpreadsheetError(message: String, subject: String = "Erro ao buscar as planilhas") =
        mailerImpl.send(
            Mail(
                to = Configuration.email.devs,
                cc = Configuration.email.cc,
                subject = subject,
                body = message,
            ),
        )
}
