package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.valiktor.Constraint
import org.valiktor.Validator
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

        val bonds = listOf("Aluno de doutorado",
            "Docente",
            "Docente Sênior",
            "Funcionário",
            "PART (Programa de Atração e Retenção de Talentos)",
            "Pesquisador (Pós-doutorando)",
            "Professor Contratado")

        fun createFrom(subrow: List<String?>) = Researcher(
            name = subrow[2],
            email = subrow[3],
            unities = readSet(subrow[5]),
            keywords = readSet(subrow[28]),
            lattes = subrow[29],
            photo = subrow[30] ?: "",
            skills = splitUnlessND(subrow[23]),
            services = splitUnlessND(subrow[24]),
            equipments = splitUnlessND(subrow[25]),
            phone = read(subrow[31]),
            limitDate = read(subrow[36]),
            bond = subrow[1],
            campus = subrow[6],
            area = KnowledgeAreas.createFrom(subrow)
        )

        fun splitUnlessND(term: String?) : Set<String>? {
            if(term == "N/D" || term == null ) return emptySet()
            else return term?.split(";")?.map { it.trim() }?.toSet()
        }

        fun readSet(term: String?) : Set<String>? {
            if(term == null) return null
            else return term?.split(";")?.map { it.trim() }?.toSet()
        }

        fun read(term: String?) : String? {
            if(term == null) return "N/D"
            else return term
        }

    }

    init {
        try {
            validate(this) {
                validate(Researcher::name)
                    .isNotNull()
                    .isNotBlank()

                validate(Researcher::email)
                    .isNotNull()
                    .isEmail()

                validate(Researcher::unities)
                    .isNotNull()
                    .isValidUnities()

                validate(Researcher::keywords)
                    .isNotNull()
                    .isNotEmpty()

                validate(Researcher::lattes)
                    .isNotNull()
                    .isWebsite()

                validate(Researcher::services)
                    .isNotNull()

                validate(Researcher::equipments)
                    .isNotNull()

                validate(Researcher::phone)
                    .isNotNull()

                validate(Researcher::limitDate)
                    .isNotNull()
                    .isDate()

                validate(Researcher::bond)
                    .isNotNull()
                    .isValidBond()

                validate(Researcher::campus)
                    .isNotNull()
                    .isCampus()

                validate(Researcher::skills)
                    .isNotNull()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}

@kotlinx.serialization.Serializable
data class KnowledgeAreas(
    val major: Set<String>?,
    val minors: Set<String>?
) {
    companion object {
        fun createFrom(subrow:List<String?>) = KnowledgeAreas(
            major = readSet(subrow[26]),
            minors = readSet(subrow[27])
        )

        fun readSet(term: String?) : Set<String>? {
            if(term == null) return null
            else return term?.split(";")?.map { it.trim() }?.toSet()
        }
    }

    init{
        try{
            validate(this){
                validate(KnowledgeAreas::major)
                    .isNotNull()
                    .isValidArea()

                validate(KnowledgeAreas::minors)
                    .isNotNull()
                    .isValidSubArea(major)
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}
