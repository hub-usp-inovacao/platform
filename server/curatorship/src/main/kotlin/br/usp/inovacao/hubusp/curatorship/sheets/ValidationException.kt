package br.usp.inovacao.hubusp.curatorship.sheets

import org.valiktor.ConstraintViolationException
import org.valiktor.i18n.mapToMessage

class ValidationException(
    val messages: Iterable<String>
) : RuntimeException(messages.joinToString("|")) {
    companion object {
        fun wrapping(cve: ConstraintViolationException): ValidationException {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            return ValidationException(messages = violations)
        }
    }
}