package br.usp.inovacao.hubusp.curatorship

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.mailer.Mail
import org.slf4j.LoggerFactory

class Mailer(user: String, password: String) {
    private val mailerImpl: br.usp.inovacao.hubusp.mailer.Mailer
    private val logger = LoggerFactory.getLogger(Mailer::class.java)

    init {
        mailerImpl = br.usp.inovacao.hubusp.mailer.Mailer(user, password)
    }

    fun notifySpreadsheetError(message: String, subject: String = "Erro ao buscar as planilhas") {
        if (Configuration.email.devs.none { it.isNotBlank() }) {
            logger.warn("Skipping spreadsheet error email because HUB_DEVS_EMAIL is empty: $message")
            return
        }

        mailerImpl.send(
            Mail(
                to = Configuration.email.devs,
                cc = Configuration.email.cc,
                subject = subject,
                body = message,
            ),
        )
    }
}
