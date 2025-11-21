package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.*
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import br.usp.inovacao.hubusp.curatorship.sheets.utils.indexToColumnLetter

@OptIn(kotlinx.serialization.ExperimentalSerializationApi::class)
@Serializable
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
    val keywords: Set<String>?,
) {
    companion object {
        val categories = listOf("CEPID", "EMBRAPII", "INCT", "NAP", "Centro de Pesquisa em Engenharia")

        val propertyToColumn: Map<String, String> = mapOf(
            "category" to indexToColumnLetter(0),
            "name" to indexToColumnLetter(1),
            "campus" to indexToColumnLetter(3),
            "unity" to indexToColumnLetter(4),
            "site" to indexToColumnLetter(6),
            "email" to indexToColumnLetter(7),
            "phone" to indexToColumnLetter(8),
            "description" to indexToColumnLetter(11),
            "keywords" to indexToColumnLetter(14)
        )

        fun fromRow(row: List<String?>) = PDI(
            category = row[0],
            name = row[1],
            campus = row[3],
            unity = row[4],
            coordinator = row[5],
            site = row[6],
            email = row[7],
            phone = row[8],
            description = row[11],
            keywords = row[14]?.split(";")?.toSet(),
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
