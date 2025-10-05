package br.usp.inovacao.hubusp.curatorship.sheets

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
        fun fromRow(subRow: List<String?>): Initiative {
            val contact = try {
                InitiativeContact.fromRow(subRow)
            } catch (e: ValidationException) {
                val enrichedMessages = e.messages.map { "contact.${it}" }
                throw ValidationException(messages = enrichedMessages)
            }

            return Initiative(
                name = subRow[1],
                classification = subRow[0],
                localization = subRow[3],
                unity = possible_ND(subRow[4]),
                tags = splitUnlessND(subRow[5]),
                url = subRow[6],
                description = subRow[7],
                email = splitUnlessND(subRow[8]),
                contact = contact
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
            "classification" to indexToColumnLetter(0),
            "name" to indexToColumnLetter(1),
            "localization" to indexToColumnLetter(3),
            "unity" to indexToColumnLetter(4),
            "tags" to indexToColumnLetter(5),
            "url" to indexToColumnLetter(6),
            "description" to indexToColumnLetter(7),
            "email" to indexToColumnLetter(8),
            "contact.person" to indexToColumnLetter(11),
            "contact.info" to indexToColumnLetter(12)
        )

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
        fun fromRow(subRow: List<String?>) = InitiativeContact(
            person = possible_ND(subRow[11]),
            info = subRow[12]
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
