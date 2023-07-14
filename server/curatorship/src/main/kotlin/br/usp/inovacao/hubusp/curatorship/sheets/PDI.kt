package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
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

@Serializer(forClass = Time::class)
object TimeSerializer : KSerializer<Time> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Time", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Time) {
        encoder.encodeString(value.time.format(formatter))
    }

    override fun deserialize(decoder: Decoder): Time {
        return Time(LocalDateTime.parse(decoder.decodeString(), formatter))
    }
}

@kotlinx.serialization.Serializable
data class PDI(
    val category: String?,
    val name: String?,
    val unity: String?,
    val campus: String?,
    val coordinator: String?,
    val phone: String?,
    val email: String?,
    val description: String?,
    val site: String?,
    val keywords: Set<String>?,
    @Contextual val timestamp: Time
) {
    companion object {
        val categories = listOf("CEPID", "EMBRAPII", "INCT", "NAP", "Centro de Pesquisa em Engenharia")

        fun fromRow(row: List<String?>) = PDI(
            category = row[0],
            name = row[1],
            campus = row[3],
            unity = row[4],
            coordinator = row[5],
            site = row[6],
            email = row[7],
            phone = row[8],
            description = row[11],
            keywords = row[14]?.split(";")?.toSet(),
            timestamp = Time( time = LocalDateTime.now())
        )
    }

    init {
        try {
            validate(this) {
                validate(PDI::category)
                    .isNotNull()
                    .isPDICategory()

                validate(PDI::name)
                    .isNotNull()
                    .isNotBlank()

                validate(PDI::unity)
                    .isNotNull()
                    .isUnity()

                validate(PDI::campus)
                    .isNotNull()
                    .isCampus()

                validate(PDI::phone)
                    .isPhone()

                validate(PDI::email)
                    .isEmail()

                validate(PDI::description)
                    .isNotNull()
                    .isNotBlank()

                validate(PDI::site)
                    .isWebsite()

                validate(PDI::keywords)
                    .isNotNull()
                    .isNotEmpty()
            }
        } catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "messages")
                .map { "${it.property}: ${it.message}" }

            throw ValidationException(messages = violations)
        }
    }
}

@Serializable
data class Time(
    @Contextual val time: LocalDateTime,
)