package br.usp.inovacao.hubusp.curatorship.sheets

import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.validate
import org.valiktor.functions.*
import org.valiktor.i18n.mapToMessage

@Serializable
data class Initiative(
    val classification: String,
    val name: String,
    val localization: String,
    val unity: String? = null,
    val tags: Set<String>,
    val url: String? = null,
    val description: String,
    val email: String? = null,
    val contact: InitiativeContact,
) {
    companion object {
        val classifications = listOf("Agente Institucional", "Empresa Jr.", "Entidade Associada", "Entidade Estudantil", "Espaço/Coworking", "Grupos e Iniciativas Estudantis", "Ideação", "Incubadora e Parque Tecnológico")

        fun fromRow(row: List<String?>) = Initiative(
            classification = row[0]!!,
            name = row[1]!!,
            localization = row[2]!!,
            unity = row[3],
            tags = row[4]!!.split(";").toSet(),
            url = row[5],
            description = row[6]!!,
            email = row[7],
            contact = InitiativeContact(info = row[8], person = row[9])
        )
    }

    init {
        try {
            validate(this) {
                validate(Initiative::classification)
                    .isInitiativeClassification()

                validate(Initiative::name)
                    .isNotBlank()

                validate(Initiative::localization)
                    .isCampus()

                validate(Initiative::unity)
                    .isUnity()

                validate(Initiative::tags)
                    .isNotEmpty()

                validate(Initiative::url)
                    .isWebsite()

                validate(Initiative::description)
                    .isNotBlank()

                validate(Initiative::email)
                    .isEmail()

                validate(Initiative::contact).validate {
                    validate(InitiativeContact::info)
                        .isPhone()
                }

                validate(Initiative::contact).validate {
                    validate(InitiativeContact::person)
                        .isNotBlank()
                }
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
data class InitiativeContact(
    val info: String? = null,
    val person: String? = null
)
