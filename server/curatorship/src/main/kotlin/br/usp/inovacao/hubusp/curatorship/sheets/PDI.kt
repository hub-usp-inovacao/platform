package br.usp.inovacao.hubusp.curatorship.sheets

import org.valiktor.ConstraintViolationException
import org.valiktor.functions.*
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

@kotlinx.serialization.Serializable
data class PDI(
    val category: String?,
    val name: String?,
    val unity: String?,
    val campus: String?,
    val coordinator: String?,
    val phone: String?,
    val email: String?,
    val description: String?,
    val site: String?,
    val keywords: Set<String>?
) {
    companion object {
        val categories = listOf("CEPID", "EMBRAPII", "INCT", "NAP", "Centro de Pesquisa em Engenharia")
    }

    init {
        try {
            validate(this) {
                validate(PDI::category)
                    .isNotNull()
                    .isPDICategory()

                validate(PDI::name)
                    .isNotNull()
                    .isNotBlank()

                validate(PDI::unity)
                    .isNotNull()
                    .isUnity()

                validate(PDI::campus)
                    .isNotNull()
                    .isCampus()

                validate(PDI::phone)
                    .isPhone()

                validate(PDI::email)
                    .isEmail()

                validate(PDI::description)
                    .isNotNull()
                    .isNotBlank()

                validate(PDI::site)
                    .isWebsite()

                validate(PDI::keywords)
                    .isNotNull()
                    .isNotEmpty()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}
