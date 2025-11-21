package br.usp.inovacao.hubusp.curatorship.sheets

import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isWebsite
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import br.usp.inovacao.hubusp.curatorship.sheets.utils.indexToColumnLetter
import br.usp.inovacao.hubusp.curatorship.sheets.utils.formatUrl

@kotlinx.serialization.Serializable
data class Area(
    val cip: String?,
    val subarea: String?
){
    companion object {
        fun fromRow(subRow: List<String?>) =
            Area(
                cip = subRow[0],
                subarea = subRow[1],
            )
    }

    init {
        try {
            validate(this) {
                validate(Area::cip)
                    .isNotBlank()
                    .isValidCip()

                validate(Area::subarea)
                    .isNotBlank()
                    .isValidSubarea()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> =
                cve.constraintViolations.mapToMessage(baseName = "messages").map {
                    "${it.property}: ${it.message}"
                }

            throw ValidationException(messages = violations)
        }
    }
}

@kotlinx.serialization.Serializable
data class DualClassification(
    val primary: Area,
    val secondary: Area? = null
) {
    companion object {
        fun fromRow(row: List<String?>) =
            DualClassification(
                primary = Area.fromRow(row.subList(0, 2)),
                secondary = if (row[2].isNullOrBlank() && row[3].isNullOrBlank()) {
                    null
                } else {
                    Area.fromRow(row.subList(2, 4))
                }
            )
    }
}

@kotlinx.serialization.Serializable
data class Patent(
    val classification: DualClassification,
    val countriesWithProtection: Set<String>,
    val inventors: Set<String>,
    val ipcs: Set<String>,
    val name: String,
    val owners: Set<String>,
    val photo: String? = null,
    val status: String,
    val summary: String,
    val url: String? = null
) {
    companion object {

        val validStatuses = listOf("Concedida", "Em análise", "Domínio Público")

        val propertyToIndex: Map<String, Int> = mapOf(
            "classification.primary.cip" to 0,
            "classification.primary.subarea" to 1,
            "classification.secondary.cip" to 2,
            "classification.secondary.subarea" to 3,
            "name" to 5,
            "ipcs" to 6,
            "owners" to 8,
            "inventors" to 9,
            "summary" to 10,
            "countriesWithProtection" to 11,
            "status" to 12,
            "url" to 14,
            "photo" to 15
        )

        val propertyToColumn = propertyToIndex.mapValues { indexToColumnLetter(it.value) }

        private fun createImageUrl(id: String?): String? {
            return if (id == "N/D" || id.isNullOrBlank()) {
                null
            } else {
                "https://drive.google.com/uc?export=view&id=$id"
            }
        }

        private fun splitString(raw: String?): Set<String> {
            return raw?.split(" | ")
                ?.map { it.trim() }
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

        fun fromRow(row: List<String?>): Patent {

            val classificationRow = row.subList(propertyToIndex["classification.primary.cip"]!!,
                propertyToIndex["classification.secondary.subarea"]!! + 1)

            return Patent(
                classification = DualClassification.fromRow(classificationRow),
                name = row[propertyToIndex["name"]!!] ?: "",
                ipcs = splitString(row[propertyToIndex["ipcs"]!!]),
                owners = splitString(row[propertyToIndex["owners"]!!]),
                inventors = splitString(row[propertyToIndex["inventors"]!!]),
                summary = row[propertyToIndex["summary"]!!] ?: "",
                countriesWithProtection = splitString(row[propertyToIndex["countriesWithProtection"]!!]),
                status = row[propertyToIndex["status"]!!] ?: "",
                url = formatUrl(row[propertyToIndex["url"]!!]),
                photo = createImageUrl(row.getOrNull(propertyToIndex["photo"]!!))
            )
        }
    }
    init {
        try {
            validate(this) {
                validate(Patent::name).isNotBlank()
                validate(Patent::status).isNotBlank()

                validate(Patent::url).isWebsite()
                validate(Patent::photo).isWebsite()

                validate(Patent::status).isValidStatus()

                validate(Patent::ipcs).isValidIpcs()

            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> =
                cve.constraintViolations.mapToMessage(baseName = "messages").map {
                    "${it.property}: ${it.message}"
                }

            throw ValidationException(messages = violations)
        }
    }
}