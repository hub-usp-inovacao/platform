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

        fun fromRow(row: List<String?>) = PDI(
            category = row[0],
            name = row[1],
            campus = row[2],
            unity = row[3],
            coordinator = row[4],
            site = row[5],
            email = row[6],
            phone = row[7],
            description = row[8],
            keywords = row[9]?.split(";")?.toSet()
        )
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
