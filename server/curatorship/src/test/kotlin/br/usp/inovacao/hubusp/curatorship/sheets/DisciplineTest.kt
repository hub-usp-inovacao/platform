package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.selects.html5.*
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DisciplineTest {
    @Test
    fun `it can fix Jupiter URLS`() {
        val baseUrl = "https://uspdigital.usp.br/jupiterweb/obterTurma?sgldis=MAC0430"
        assertEquals(Discipline.fixJupiterUrl("${baseUrl}&verdis=5"), baseUrl)
        assertEquals(Discipline.fixJupiterUrl("${baseUrl}&verdis=52&foobar"), "${baseUrl}&foobar")
    }
}
