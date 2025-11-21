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

        val propertyToIndex: Map<String, Int> = mapOf(
            "category" to 0,
            "name" to 1,
            "campus" to 3,
            "unity" to 4,
            "coordinator" to 5,
            "site" to 6,
            "email" to 7,
            "phone" to 8,
            "description" to 11,
            "keywords" to 14
        )

        val propertyToColumn = propertyToIndex.mapValues { indexToColumnLetter(it.value) }

        fun fromRow(row: List<String?>) = PDI(
            category = row[propertyToIndex["category"]!!],
            name = row[propertyToIndex["name"]!!],
            campus = row[propertyToIndex["campus"]!!],
            unity = row[propertyToIndex["unity"]!!],
            coordinator = row[propertyToIndex["coordinator"]!!],
            site = row[propertyToIndex["site"]!!],
            email = row[propertyToIndex["email"]!!],
            phone = row[propertyToIndex["phone"]!!],
            description = row[propertyToIndex["description"]!!],
            keywords = row[propertyToIndex["keywords"]!!]?.split(";")?.toSet(),
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
