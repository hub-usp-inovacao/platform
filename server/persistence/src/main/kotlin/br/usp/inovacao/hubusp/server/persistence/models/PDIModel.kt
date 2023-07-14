package br.usp.inovacao.hubusp.server.persistence.models

import br.usp.inovacao.hubusp.server.catalog.Time
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.Contextual
import java.sql.Timestamp
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Serializer(forClass = PDITime::class)
object PDITimeSerializer : KSerializer<PDITime> {
    private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("PDITime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: PDITime) {
        encoder.encodeString(value.time.format(formatter))
    }

    override fun deserialize(decoder: Decoder): PDITime {
        return PDITime(LocalDateTime.parse(decoder.decodeString(), formatter))
    }
}

@Serializable
data class PDIModel(
    val category: String,
    val name: String,
    val campus: String,
    val unity: String,
    val coordinator: String,
    val site: String,
    val email: String,
    val phone: String,
    val description: String,
    val tags: Set<String>,
    val timestamp: PDITime,
)

@Serializable
data class PDITime(
    @Contextual val time: LocalDateTime,
)