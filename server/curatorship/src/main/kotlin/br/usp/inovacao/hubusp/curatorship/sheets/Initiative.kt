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
    var email: String? = null,
    var contact: Contact
) {
    companion object {
        fun fromRow(subRow: List<String?>) = Initiative(
            name = subRow[1],
            classification = subRow[0],
            localization = subRow[3],
            unity = subRow[4],
            tags = get_tags(subRow[5]),
            url = possible_ND(subRow[6]),
            description = subRow[7],
            email = possible_ND(subRow[8]),
            contact = Contact.fromRow(subRow)
        )

        fun possible_ND(term: String?) : String? {
            if(term == "N/D") return null
            else return term
        }

        fun get_tags(raw: String?): Set<String> {
            val isEmpty = raw.isNullOrEmpty()

            return if (isEmpty) {
                emptySet()
            } else {
                raw!!.split(";").toSet()
            }
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
                    .isCampus()

                validate(Initiative::unity)
                    .isNotNull()
                    .isUnity()

                validate(Initiative::tags)
                    .isNotNull()

                validate(Initiative::url)
                    .isNotNull()
                    .isNotBlank()

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
data class Contact(
    val info: String?,
    val person: String?
) {
    companion object {
        fun fromRow(subRow: List<String?>) = Contact(
            info = subRow[11],
            person = subRow[12]
        )
    }

    init {
        try {
            validate(this) {
                validate(Contact::info)
                    .isNotNull()
                    .isNotBlank()
                    .isPhoneOrEmail()

                validate(Contact::person)
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
