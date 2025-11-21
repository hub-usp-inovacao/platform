package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.sheets.Researcher.Companion.propertyToIndex
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.*
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import br.usp.inovacao.hubusp.curatorship.sheets.utils.indexToColumnLetter

@kotlinx.serialization.Serializable
data class Initiative(
    var name: String?,
    var classification: String?,
    var localization: String?,
    var unity: String?,
    var tags: Set<String>?,
    var url: String? = null,
    var description: String?,
    var email: Set<String>? = null,
    var contact: InitiativeContact
) {
    companion object {

        val propertyToIndex: Map<String, Int> = mapOf(
            "classification" to 0,
            "name" to 1,
            "localization" to 3,
            "unity" to 4,
            "tags" to 5,
            "url" to 6,
            "description" to 7,
            "email" to 8,
            "contact.person" to 11,
            "contact.info" to 12
        )

        val propertyToColumn = propertyToIndex.mapValues { indexToColumnLetter(it.value) }

        fun fromRow(subRow: List<String?>): Initiative {

            val personRaw = subRow[propertyToIndex["contact.person"]!!]
            val infoRaw = subRow[propertyToIndex["contact.info"]!!]

            val contact = try {
                InitiativeContact.fromRow(personRaw, infoRaw)
            } catch (e: ValidationException) {
                val enrichedMessages = e.messages.map { "contact.${it}" }
                throw ValidationException(messages = enrichedMessages)
            }

            return Initiative(
                name = subRow[propertyToIndex["name"]!!],
                classification = subRow[propertyToIndex["classification"]!!],
                localization = subRow[propertyToIndex["localization"]!!],
                unity = possible_ND(subRow[propertyToIndex["unity"]!!]),
                tags = splitUnlessND(subRow[propertyToIndex["tags"]!!]),
                url = subRow[propertyToIndex["url"]!!],
                description = subRow[propertyToIndex["description"]!!],
                email = splitUnlessND(subRow[propertyToIndex["email"]!!]),
                contact = contact
            )
        }

        fun possible_ND(term: String?) : String? {
            if(term == null) return "N/D"
            else return term
        }

        fun splitUnlessND(term: String?) : Set<String>? {
            if(term == "N/D" || term == null ) return emptySet()
            else return term?.split(";")?.map { it.trim() }?.toSet()
        }

    }

    init {
        try {
            validate(this) {
                validate(Initiative::name)
                    .isNotNull()
                    .isNotBlank()

                validate(Initiative::classification)
                    .isNotNull()
                    .isNotBlank()
                    .isClassification()

                validate(Initiative::localization)
                    .isNotNull()
                    .isNotBlank()
                    .isInitiativeCampus()

                validate(Initiative::unity)
                    .isNotNull()
                    .isInitiativeUnity()

                validate(Initiative::tags)
                    .isNotNull()

                validate(Initiative::url)
                    .isWebsite()

                validate(Initiative::description)
                    .isNotNull()
                    .isNotBlank()

                validate(Initiative::email)
                    .isEmail()
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
data class InitiativeContact(
    val person: String?,
    val info: String?
) {
    companion object {
        fun fromRow(personRaw: String?, infoRaw: String?) = InitiativeContact(
            person = possible_ND(personRaw),
            info = infoRaw
        )

        fun possible_ND(term : String?) : String? {
            if(term == null) return "N/D"
            else return term
        }
    }

    init {
        try {
            validate(this) {
                validate(InitiativeContact::person)
                    .isNotNull()
                    .isNotBlank()

                validate(InitiativeContact::info)
                    .isPhone()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}
