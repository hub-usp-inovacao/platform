package br.usp.inovacao.hubusp.curatorship.sheets

import org.valiktor.ConstraintViolationException
import org.valiktor.functions.*
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate

@kotlinx.serialization.Serializable
data class Researcher(
        val name: String?,
        val email: String?,
        val unities: Set<String>?,
        val keywords: Set<String>?,
        val lattes: String?,
        val photo: String?,
        val skills: Set<String>?,
        val services: Set<String>?,
        val equipments: Set<String>?,
        val phone: String?,
        val limitDate: String?,
        val bond: String?,
        val campus: String?,
        val area: KnowledgeAreas
) {

    companion object {

        val bonds =
                listOf(
                        "Aluno de doutorado",
                        "Docente",
                        "Docente Sênior",
                        "Funcionário",
                        "PART (Programa de Atração e Retenção de Talentos)",
                        "Pesquisador (Pós-doutorando)",
                        "Professor Contratado"
                )

        fun createFrom(subrow: List<String?>): Researcher {
            val area = try {
                KnowledgeAreas.createFrom(subrow)
            } catch (e: ValidationException) {
                val enrichedMessages = e.messages.map { "area.${it}" }
                throw ValidationException(messages = enrichedMessages)
            }

            return Researcher(
                name = subrow[2],
                email = subrow[3],
                unities = readSet(subrow[5]),
                keywords = readSet(subrow[28]),
                lattes = subrow[29],
                photo = formatPhoto(subrow[30]),
                skills = splitUnlessND(subrow[23]),
                services = splitUnlessND(subrow[24]),
                equipments = splitUnlessND(subrow[25]),
                phone = read(subrow[31]),
                limitDate = read(subrow[36]),
                bond = subrow[1],
                campus = subrow[6],
                area = area
            )
        }

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
            "bond" to indexToColumnLetter(1),
            "name" to indexToColumnLetter(2),
            "email" to indexToColumnLetter(3),
            "unities" to indexToColumnLetter(5),
            "campus" to indexToColumnLetter(6),
            "skills" to indexToColumnLetter(23),
            "services" to indexToColumnLetter(24),
            "equipments" to indexToColumnLetter(25),
            "area.major" to indexToColumnLetter(26),
            "area.minors" to indexToColumnLetter(27),
            "keywords" to indexToColumnLetter(28),
            "lattes" to indexToColumnLetter(29),
            "phone" to indexToColumnLetter(31),
            "limitDate" to indexToColumnLetter(36)
        )

        fun formatPhoto(raw: String?): String? {

            if (raw == null || raw == "N/D") {
                return null
            }

            if (raw.length > 3 && raw.substring(0..3) == "http") return raw

            return "https://drive.google.com/thumbnail?id=$raw"
        }

        fun splitUnlessND(term: String?): Set<String>? {
            if (term == "N/D" || term == null) return emptySet()
            else return term?.split(";")?.map { it.trim() }?.toSet()
        }

        fun readSet(term: String?): Set<String>? {
            if (term == null) return null else return term?.split(";")?.map { it.trim() }?.toSet()
        }

        fun read(term: String?): String? {
            if (term == null) return "N/D" else return term
        }
    }

    init {
        try {
            validate(this) {
                validate(Researcher::name).isNotNull().isNotBlank()

                validate(Researcher::email).isNotNull().isEmail()

                validate(Researcher::unities).isNotNull().isValidUnities()

                validate(Researcher::keywords).isNotNull().isNotEmpty()

                validate(Researcher::lattes).isNotNull().isWebsite()

                validate(Researcher::services).isNotNull()

                validate(Researcher::equipments).isNotNull()

                validate(Researcher::phone).isNotNull()

                validate(Researcher::limitDate).isNotNull().isDate()

                validate(Researcher::bond).isNotNull().isValidBond()

                validate(Researcher::campus).isNotNull().isCampus()

                validate(Researcher::skills).isNotNull()
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
data class KnowledgeAreas(val major: Set<String>?, val minors: Set<String>?) {
    companion object {
        fun createFrom(subrow: List<String?>) =
                KnowledgeAreas(major = readSet(subrow[26]), minors = readSet(subrow[27]))

        fun readSet(term: String?): Set<String>? {
            if (term == null) return null else return term?.split(";")?.map { it.trim() }?.toSet()
        }
    }

    init {
        try {
            validate(this) {
                validate(KnowledgeAreas::major).isNotNull().isValidArea()

                validate(KnowledgeAreas::minors).isNotNull().isValidSubArea(major)
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
