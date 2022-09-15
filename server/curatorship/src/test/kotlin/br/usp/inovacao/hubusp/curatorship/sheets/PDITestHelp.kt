package br.usp.inovacao.hubusp.curatorship.sheets

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
    }
}