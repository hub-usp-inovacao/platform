package br.usp.inovacao.hubusp.curatorship.sheets

fun Initiative.toRow(): List<String?> = listOf(
    classification,
    name,
    localization,
    unity,
    tags.joinToString(";"),
    url,
    description,
    email,
    contact.info,
    contact.person
)

class InitiativeTestHelp {
    companion object {
        val VALID_RECORD = Initiative(
            classification = "Agente Institucional",
            name = "Inova.USP",
            localization = "Butantã",
            unity = null,
            tags = setOf("COSMOS", "IRIS", "Novos projetos"),
            url = null,
            description = "O Centro de Inovação",
            email = null,
            contact = InitiativeContact(info = null, person = null)
        )

        fun validRowAndInvalidRow(): List<List<String?>> {
            val validRow = VALID_RECORD.toRow()
            val invalidRow = VALID_RECORD.toRow().toMutableList()
            invalidRow[0] = "Wrong Classification"

            return listOf(validRow, invalidRow)
        }

        val validRow: List<String?> = listOf(
            "Agente Institucional",
            "Inova.USP",
            "Butantã",
            null,
            "COSMOS;IRIS;Novos projetos",
            null,
            "O Centro de Inovação",
            null,
            null,
            null
        )
    }
}
