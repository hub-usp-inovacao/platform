package br.usp.inovacao.hubusp.curatorship.sheets


fun Initiative.toRow(): List<String?> = listOf(
    classification,
    name,
    description,
    localization,
    unity,
    contact.person,
    url,
    email,
    contact.info,
    null,
    null,
    null,
    null,
    null,
    tags?.joinToString(";")
)

class InitiativesTestHelp {
    companion object {
        val VALID_RECORD = Initiative(
            classification = "Pesquisa",
            name = "Iniciativa Teste",
            description = "Descrição da iniciativa",
            localization = "Campus Butantã",
            unity = "Faculdade de Filosofia, Letras e Ciências Humanas",
            contact = InitiativeContact(
                info = "Informações de contato",
                person = "Pessoa de Contato"
            ),
            url = "https://example.com",
            email = "contato@example.com",
            tags = setOf("inovação", "tecnologia")
        )

        fun validRowAndInvalidRow(): List<List<String?>> {
            val header = listOf(
                "CLASSIFICAÇÃO", "NOME", "DESCRIÇÃO", "LOCALIZAÇÃO",
                "UNIDADE", "CONTATO PESSOA", "URL", "EMAIL",
                "CONTATO INFO", "OUTRO", "OUTRO", "OUTRO",
                "OUTRO", "OUTRO", "TAGS"
            )
            val validRow = VALID_RECORD.toRow()
            val invalidRow = VALID_RECORD.toRow().toMutableList()
            invalidRow[0] = "Classificação Errada"

            return listOf(header, validRow, invalidRow)
        }

        val validRow: List<String?> = VALID_RECORD.toRow()
    }
}
