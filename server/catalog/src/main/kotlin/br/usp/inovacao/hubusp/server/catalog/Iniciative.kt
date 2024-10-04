package br.usp.inovacao.hubusp.server.catalog
@kotlinx.serialization.Serializable

import org.bson.types.ObjectId
import org.litote.kmongo.Id
import org.litote.kmongo.newId
import org.litote.kmongo.id.WrappedObjectId

data class Iniciative(
    val id: Id<Iniciative> = newId(),
    var name: String,
    var classification: String,
    var localization: String,
    var unity: String? = null,
    var tags: List<String>? = null,
    var url: String? = null,
    var description: String? = null,
    var email: String? = null,
    var contact: Map<String, String>? = null
) {
    init {
        require(name.isNotBlank()) { "Name cannot be blank" }
        require(classification.isNotBlank()) { "Classification cannot be blank" }
        require(localization.isNotBlank()) { "Localization cannot be blank" }

        if (!url.isNullOrEmpty()) {
            require(isValidUrl(url)) { "Invalid URL" }
        }

        if (contact != null) {
            require(validContact(contact["info"] ?: "")) {
                "Invalid contact. Provide a valid phone number or email."
            }
        }

        if (!description.isNullOrEmpty()) {
            require(description is String && description.isNotBlank()) {
                "Invalid description"
            }
        }

        require(unity == "N/D" || validUnity(unity)) { "Invalid unity" }
        require(validLocalization(localization)) { "Invalid localization" }
        require(validClassification(classification)) { "Invalid classification" }
    }

    companion object {
        private fun validContact(info: String): Boolean {
            val phoneRegex = Regex("^\\d{8,13}\$")
            val emailRegex = Regex("^(|(([A-Za-z0-9]+_+)|([A-Za-z0-9]+-+)|([A-Za-z0-9]+\\.+)|([A-Za-z0-9]+\\++)*[A-Za-z0-9]+@((\\w+-+)|(\\w+\\.))*\\w{1,63}\\.[a-zA-Z]{2,6}))$")
            return info.isNotEmpty() && (info.replace("\\D".toRegex(), "").matches(phoneRegex) || info.matches(emailRegex) || info == "N/D")
        }

        private fun isValidUrl(url: String): Boolean {
            val urlRegex = Regex("^(https?|ftp)://[^\\s/$.?#].[^\\s]*\$")
            return url.matches(urlRegex)
        }

        private fun validUnity(unity: String?): Boolean {
            val validUnities = listOf("Unity1", "Unity2", "N/D") // Define valid unity values
            return unity in validUnities
        }

        private fun validLocalization(localization: String): Boolean {
            val validLocalizations = listOf("Campus1", "Campus2") // Define valid campus names
            return localization in validLocalizations
        }

        private fun validClassification(classification: String): Boolean {
            val validClasses = listOf(
                "Agente Institucional",
                "Empresa Jr.",
                "Entidade Associada",
                "Entidade Estudantil",
                "Espaço/Coworking",
                "Grupos e Iniciativas Estudantis",
                "Ideação",
                "Incubadora e Parque Tecnológico"
            )
            return classification in validClasses
        }

        fun createFrom(row: List<String>): Iniciative {
            return Iniciative(
                classification = row[0],
                name = row[1],
                localization = row[3],
                unity = row[4],
                tags = row[5].takeIf { it.isNotEmpty() }?.split(";"),
                url = row[6].takeIf { it != "N/D" },
                description = row[7],
                email = row[8].takeIf { it != "N/D" },
                contact = getContact(row[11], row[12])
            ).also {
                if (!it.isValid()) throw IllegalArgumentException(it.validationErrors().joinToString())
            }
        }

        private fun getContact(person: String?, info: String?): Map<String, String>? {
            return if (person.isNullOrBlank() && info.isNullOrBlank()) null else mapOf("person" to person!!, "info" to info!!)
        }
    }

    fun isValid(): Boolean {
        return true
    }

    fun validationErrors(): List<String> {
        return emptyList()
    }
}
