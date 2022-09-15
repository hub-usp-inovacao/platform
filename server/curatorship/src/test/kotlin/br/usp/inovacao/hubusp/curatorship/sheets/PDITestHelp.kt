package br.usp.inovacao.hubusp.curatorship.sheets

fun PDI.toRow(): List<String?> = listOf(
    category,
    name,
    campus,
    unity,
    coordinator,
    site,
    email,
    phone,
    description,
    keywords?.joinToString(";")
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
            keywords = setOf("foo", "baz")
        )

        fun validRowAndInvalidRow(): List<List<String?>> {
            val validRow = VALID_RECORD.toRow()
            val invalidRow = VALID_RECORD.toRow().toMutableList()
            invalidRow[0] = "Wrong Category"

            return listOf(validRow, invalidRow)
        }
    }
}