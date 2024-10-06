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
import java.time.LocalDate

@kotlinx.serialization.Serializable

data class Researcher(
    val name: String?,
    val email: String?,
    val unities: Set<String>?,
    val keywords: Set<String>?,
    val lattes: String?,
    val photo: String? = null,
    val skills: Set<String>?,
    val services: Set<String>?,
    val equipments: Set<String>?,
    val phone: String? = null,
    @Contextual val limitDate: LocalDate? = null,
    val bond: String?,
    val campus: String?,
    val area: KnowledgeAreas
) {

    companion object { 
        fun createFrom(subrow: List<String?>) = Researcher(
            name = subrow[2],
            email = subrow[3],
            unities = subrow[5]?.split(";")?.toSet() ?: emptySet(),
            keywords = subrow[28]?.split(";")?.toSet() ?: emptySet(),
            lattes = subrow[29],
            photo = subrow[30] ?: "",
            skills = splitUnlessND(subrow[23]),
            services = splitUnlessND(subrow[24]),
            equipments = splitUnlessND(subrow[25]), //mismatch
            phone = subrow[31],
            limitDate = subrow[36],
            bond = subrow[1],
            campus = subrow[6],
            area = KnowledgeAreas.createFrom(subrow)
        )

        fun splitUnlessND(term: String?) : Set<String> {
            if(term == "N/D") return emptySet()
            else return term?.split(";")?.toSet() ?: emptySet()
        }

    }

    init {
        try {
            validate(this) {
                validate(Researcher::name)
                    .isNotNull()
                    .isNotBlank()

                validate(Researcher::email)
                    .isEmail()

                validate(Researcher::unities)
                    .isNotNull()
                    .isUnity()

                validate(Researcher::keywords)
                    .isNotNull()
                    .isNotEmpty()

                validate(Researcher::lattes)
                    .isWebsite()

                validate(Researcher::Researchers)
                    .isNotNull()
                    .isNotEmpty()

                validate(Researcher::services)
                    .isNotNull()
                    .isNotEmpty()

                validate(Researcher::equipments)
                    .isNotNull()
                    .isNotEmpty()

                validate(Researcher::phones)
                    .isPhone()

                validate(Researcher::limitDate)
                    .isNotNull()

                validate(Researcher::bond)
                    .isNotNull()
                    .isNotBlank()

                validate(Researcher::campus)
                    .isNotNull()
                    .isCampus()

                validate(Researcher::researchers)
                    .isNotNull()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: Set<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}

@kotlinx.serialization.Serializable
data class KnowledgeAreas(
    val area: Set<String>,
    val subArea: Set<String>
) {
    companion object {
        fun createFrom(subrow:Set<String>) = KnowledgeAreas(
            area = subrow[26],
            subArea = subrow[27]
        )
    }

    init{
        try{
            validate(this){
                validate(KnowledgeAreas::area)
                    .isNotNull()
                    .isValidArea()
                
                validate(KnowledgeAreas::subArea)
                    .isNotNull()
                    .isValidSubArea(area)
            }
        } catch (cve: ConstraintViolationException) {
            val violations: Set<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}
