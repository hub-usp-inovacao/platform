package br.usp.inovacao.hubusp.curatorship

@kotlinx.serialization.Serializable
data class PDI(
    val category: PDICategory,
    val name: String
) { 
    companion object {
        fun from(category: String, name: String): PDI {
            ensureNameIsNotEmpty(name)
            val pdiCategory = PDICategory.from(category)
            return PDI(category = pdiCategory, name = name)
        }

        private fun ensureNameIsNotEmpty(name: String) {
            if (name.isEmpty()) throw ValidationException("Name cannot be empty")
        }
    }
}

enum class PDICategory {
    CEPID, EMBRAPII, INCT, NAP, EngineeringCenter;

    companion object {
        fun from(value: String) = when (value) {
            "CEPID" -> CEPID
            "EMBRAPII" -> EMBRAPII
            "INCT" -> INCT
            "NAP" -> NAP
            "Centro de Pesquisa em Engenharia" -> EngineeringCenter

            else -> throw ValidationException("$value is not accepted as PDICategory")
        }
    }
}

class ValidationException(msg: String) : RuntimeException(msg)

fun main() {
    val pdi = PDI.from("NAP", "Briza")
}

//data class PDI(
//    val category: String,
//    val name: String,
//    val campus: String,
//    val unity: String,
//    val coordinator: String,
//    val site: String,
//    val email: String,
//    val phone: String,
//    val description: String,
//    val tags: Set<String>,
//)