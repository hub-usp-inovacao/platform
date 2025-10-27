package br.usp.inovacao.hubusp.curatorship.companyform.step

import java.util.Locale
import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage

class StepValidationException(val messages: List<String>) :
    RuntimeException(messages.joinToString("|")) {
    companion object {
        fun from(cve: ConstraintViolationException) =
            StepValidationException(
                messages =
                    cve.constraintViolations
                        .mapToMessage(baseName = "messages", locale = Locale("pt", "BR"))
                        .map { "${it.property}: ${it.message}" },
            )
    }
}
