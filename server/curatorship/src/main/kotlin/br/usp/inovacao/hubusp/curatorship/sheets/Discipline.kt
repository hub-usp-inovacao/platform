package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isWebsite
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

@Serializable
data class Category(
    val innovation: Boolean,
    val business: Boolean,
    val entrepreneurship: Boolean,
    val intellectual_property: Boolean
)

@Serializable
data class Description(
    val long: String?,
    val short: String?
)

@Serializable
data class Discipline(
    val nature: String?,
    val name: String?,
    val campus: String?,
    val unity: String?,
    val url: String?,
    val level: String?,
    val description: Description?,
    val start_date: String?,
    val end_date: String?,
    val category: Category?,
    val offeringPeriod: String?,
    val keywords: Set<String>?
) {

    companion object {
        fun fromRow(row: List<String?>) : Discipline {

            val category = Category(
                business = isSelected(row[9]),
                entrepreneurship = isSelected(row[10]),
                innovation = isSelected(row[11]),
                intellectual_property = isSelected(row[12])
            )

            val description = Description(
                short = null,
                long = row[6]
            )

            return Discipline(
                nature = row[0],
                name = row[1],
                campus = row[2],
                unity = row[3],
                url = row[4],
                level = row[5],
                description = description,
                start_date = row[7],
                end_date = row[8],
                category = category,
                offeringPeriod = row[13],
                keywords = null
            )
        }

        private fun isSelected(symbol: String?) : Boolean = symbol.equals("X", ignoreCase = true)
    }

    init {
        try {
            validate(this) {
                validate(Discipline::nature)
                    .isNotNull()
                    .isNotBlank()

                validate(Discipline::name)
                    .isNotNull()
                    .isNotBlank()

                validate(Discipline::campus)
                    .isNotNull()
                    .isNotBlank()

                validate(Discipline::unity)
                    .isNotNull()
                    .isNotBlank()

                validate(Discipline::url)
                    .isWebsite()

                validate(Discipline::level)
                    .isNotNull()
                    .isNotBlank()

                validate(Discipline::description)
                    .isNotNull()
                    .hasDescription()

                validate(Discipline::category)
                    .isNotNull()
                    .hasCategory()

                validate(Discipline::offeringPeriod)
                    .isNotNull()
                    .isNotBlank()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}