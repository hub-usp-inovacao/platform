package br.usp.inovacao.hubusp.curatorship.sheets

import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isWebsite
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

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

        private fun indexToColumnLetter(index: Int): String {
            var i = index
            var letter = ""
            while (i >= 0) {
                letter = ('A' + i % 26) + letter
                i = i / 26 - 1
            }
            return letter
        }

        val propertyToColumn: Map<String, String> = mapOf(
            "classification.primary.cip" to indexToColumnLetter(0),
            "classification.primary.subarea" to indexToColumnLetter(1),
            "classification.secondary.cip" to indexToColumnLetter(2),
            "classification.secondary.subarea" to indexToColumnLetter(3),
            "name" to indexToColumnLetter(5),
            "ipcs" to indexToColumnLetter(6),
            "owners" to indexToColumnLetter(8),
            "inventors" to indexToColumnLetter(9),
            "summary" to indexToColumnLetter(10),
            "countriesWithProtection" to indexToColumnLetter(11),
            "status" to indexToColumnLetter(12),
            "url" to indexToColumnLetter(14),
            "photo" to indexToColumnLetter(15)
        )

        private fun createImageUrl(id: String?): String? {
            return if (id == "N/D" || id.isNullOrBlank()) {
                null
            } else {
                "https://drive.google.com/uc?export=view&id=$id"
            }
        }

        private fun formatUrl(raw: String?): String? {
            if (raw.isNullOrBlank() || raw == "N/D") {
                return null
            }

            if (raw.length > 3 && raw.substring(0..3) != "http") return "https://$raw"

            return raw
        }

        private fun splitString(raw: String?): Set<String> {
            return raw?.split(" | ")
                ?.map { it.trim() }
                ?.filter { it.isNotBlank() }
                ?.toSet()
                ?: emptySet()
        }

        fun fromRow(row: List<String?>): Patent {
            return Patent(
                classification = DualClassification.fromRow(row.subList(0, 4)),
                name = row[5] ?: "",
                ipcs = splitString(row[6]),
                owners = splitString(row[8]),
                inventors = splitString(row[9]),
                summary = row[10] ?: "",
                countriesWithProtection = splitString(row[11]),
                status = row[12] ?: "",
                url = formatUrl(row[14]),
                photo = createImageUrl(row.getOrNull(15))
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