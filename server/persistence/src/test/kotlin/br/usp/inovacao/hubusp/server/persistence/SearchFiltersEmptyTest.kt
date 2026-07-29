package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import br.usp.inovacao.hubusp.server.catalog.DisciplineSearchParams
import br.usp.inovacao.hubusp.server.catalog.InitiativeSearchParams
import br.usp.inovacao.hubusp.server.catalog.PatentSearchParams
import br.usp.inovacao.hubusp.server.catalog.PDISearchParams
import br.usp.inovacao.hubusp.server.catalog.ResearcherSearchParams
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchFiltersEmptyTest {
    @Test
    fun `it returns empty company filter when no params are set`() {
        assertEquals("{}", CompanySearchParams().toCollectionFilter())
    }

    @Test
    fun `it returns empty discipline filter when no params are set`() {
        assertEquals("{}", DisciplineSearchParams().toCollectionFilter())
    }

    @Test
    fun `it returns empty initiative filter when no params are set`() {
        assertEquals("{}", InitiativeSearchParams().toCollectionFilter())
    }

    @Test
    fun `it returns empty patent filter when no params are set`() {
        assertEquals("{}", PatentSearchParams().toCollectionFilter())
    }

    @Test
    fun `it returns empty pdi filter when no params are set`() {
        assertEquals("{}", PDISearchParams().toCollectionFilter())
    }

    @Test
    fun `it returns empty researcher filter when no params are set`() {
        assertEquals("{}", ResearcherSearchParams().toCollectionFilter())
    }
}
