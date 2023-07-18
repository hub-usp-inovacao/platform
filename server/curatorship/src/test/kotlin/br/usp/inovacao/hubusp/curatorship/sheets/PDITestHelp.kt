package br.usp.inovacao.hubusp.curatorship.sheets

import java.time.LocalDateTime

fun PDI.toRow(): List<String?> = listOf(
    category,
    name,
    null,
    campus,
    unity,
    coordinator,
    site,
    email,
    phone,
    null,
    null,
    description,
    null,
    null,
    keywords?.joinToString(";"),
    timestamp.toString()
)

class PDITestHelp {
    companion object {
        val VALID_RECORD = PDI(
            category = "CEPID",
            name = "Flau flau",
            unity = "Escola de Artes, Ciências e Humanidades - EACH",
            campus = "USP Leste",
            coordinator = null,
            phone = null,
            email = null,
            description = "lorem ipsum",
            site = null,
            keywords = setOf("foo", "baz"),
            timestamp = LocalDateTime.now()
        )

        fun validRowAndInvalidRow(): List<List<String?>> {
            val header = listOf(
                "CATEGORIA", "NOME","DESCRICAO", "CAMPUS",
                "UNIDADE", "COORDENADOR", "SITE", "EMAIL",
                "TELEFONE", "DESCRICAO","DESCRICAO", "DESCRICAO",
                "AREA DO CONHECIMENTO","SERVICOS OFERECIDOS","TAGS")
            val validRow = VALID_RECORD.toRow()
            val invalidRow = VALID_RECORD.toRow().toMutableList()
            invalidRow[0] = "Wrong Category"

            return listOf(header, validRow, invalidRow)
        }

        val validRow: List<String?> = VALID_RECORD.toRow()
    }
}