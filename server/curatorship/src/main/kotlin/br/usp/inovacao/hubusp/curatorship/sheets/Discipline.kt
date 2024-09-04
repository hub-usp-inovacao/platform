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
data class DisciplineCategory(
    val business: Boolean?,
    val entrepreneurship: Boolean?,
    val innovation: Boolean?,
    val intellectual_property: Boolean?
){
    companion object{
        fun fromRow(subRow: List<String?>) = DisciplineCategory(
            // Verify if the value is null, if it is, return false
            // If it isn't, check if it is "x" (case insensitive)
            // If it is, return true, if it isn't, return false
            business = subRow[9]?.equals("x", ignoreCase = true) ?: false,
            entrepreneurship = subRow[10]?.equals("x", ignoreCase = true) ?: false,
            innovation = subRow[11]?.equals("x", ignoreCase = true) ?: false,
            intellectual_property = subRow[12]?.equals("x", ignoreCase = true) ?: false
        )
    }

    /*init{
        try{
            validate(this).isValidCategory()
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }*/
}

@kotlinx.serialization.Serializable
data class DisciplineDescription(
    val short: String?,
    val long: String?
){
    companion object{
        fun fromRow(subRow: List<String?>) = DisciplineDescription(
            short = "",
            long = subRow[6]
        )
    }

    init{
        try{
            validate(this){
                validate(DisciplineDescription::short).isBlank()
                validate(DisciplineDescription::long).isNotBlank()
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
data class Discipline(
    val name: String?,
    val campus: String?,
    val unity: String?,
    val start_date: String?,
    val nature: String?,
    val level: String?,
    val url: String?,
    val description: DisciplineDescription,
    val category: DisciplineCategory,
    val keywords: Set<String>?,
    val offeringPeriod: String?,
){
    companion object{

        fun createKeywords(subRow: List<String?>) : Set<String>? {

            var keywords = arrayListOf<String>()

            if(subRow[9] == "x") keywords.add("Negócios")
            if(subRow[10] == "x") keywords.add("Empreendedorismo")
            if(subRow[11] == "x") keywords.add("Inovação")
            if(subRow[12] == "x") keywords.add("Propriedade Intelectual")

            return keywords.toSet()
        }

        fun fromRow(subRow: List<String?>) = Discipline(
            name = subRow[1],
            campus = subRow[2],
            unity = subRow[3],
            start_date = subRow[8],
            nature = subRow[0],
            level = subRow[5],
            url = subRow[4],
            description = DisciplineDescription.fromRow(subRow),
            category = DisciplineCategory.fromRow(subRow),
            keywords = createKeywords(subRow),
            offeringPeriod = subRow[13]
        )
    }

    init{
        try{
            validate(this){
                validate(Discipline::name)
                    .isNotNull()
                    .isNotBlank()
                    .isValidName()
                validate(Discipline::campus)
                    .isNotNull()
                    .isCampus()
                validate(Discipline::unity)
                    .isNotNull()
                    .isUnity()
                validate(Discipline::nature)
                    .isNotNull()
                    .isValidNature()
                validate(Discipline::level)
                    .isNotNull()
                    .isValidLevel()
            }
        }
        catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}