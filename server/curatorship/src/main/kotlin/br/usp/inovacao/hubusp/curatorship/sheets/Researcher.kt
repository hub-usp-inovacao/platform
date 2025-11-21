package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.sheets.utils.*
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

        val propertyToIndex: Map<String, Int> = mapOf(
            "bond" to 1,
            "name" to 2,
            "email" to 3,
            "unities" to 5,
            "campus" to 6,
            "skills" to 23,
            "services" to 24,
            "equipments" to 25,
            "area.major" to 26,
            "area.minors" to 27,
            "keywords" to 28,
            "lattes" to 29,
            "photo" to 30,
            "phone" to 31,
            "limitDate" to 36
        )

        val propertyToColumn = propertyToIndex.mapValues { indexToColumnLetter(it.value) }

        fun createFrom(subrow: List<String?>): Researcher {

            val majorRaw = subrow[propertyToIndex["area.major"]!!]
            val minorsRaw = subrow[propertyToIndex["area.minors"]!!]

            val area = try {
                KnowledgeAreas.createFrom(majorRaw, minorsRaw)
            } catch (e: ValidationException) {
                val enrichedMessages = e.messages.map { "area.${it}" }
                throw ValidationException(messages = enrichedMessages)
            }

            return Researcher(
                name = subrow[propertyToIndex["name"]!!],
                email = subrow[propertyToIndex["email"]!!],
                unities = splitAndTrim(subrow[propertyToIndex["unities"]!!], ';'),
                keywords = splitAndTrim(subrow[propertyToIndex["keywords"]!!], ';'),
                lattes = subrow[propertyToIndex["lattes"]!!],
                photo = formatPhoto(subrow[propertyToIndex["photo"]!!]),
                skills = splitUnlessND(subrow[propertyToIndex["skills"]!!]),
                services = splitUnlessND(subrow[propertyToIndex["services"]!!]),
                equipments = splitUnlessND(subrow[propertyToIndex["equipments"]!!]),
                phone = handleND(subrow[propertyToIndex["phone"]!!]),
                limitDate = handleND(subrow[propertyToIndex["limitDate"]!!]),
                bond = subrow[propertyToIndex["bond"]!!],
                campus = subrow[propertyToIndex["campus"]!!],
                area = area
            )
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
        fun createFrom(majorRaw: String?, minorsRaw: String?) =
                KnowledgeAreas(major = splitAndTrim(majorRaw, ';'), minors = splitAndTrim(minorsRaw, ';'))

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
