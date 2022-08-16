package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.ResearcherSearchParams
import kotlin.test.Test
import kotlin.test.assertEquals

internal class ResearcherExtensionKtTest {
    @Test
    fun `it parses a single major area`() {
        // given
        val major = "Engenharias"
        val params = ResearcherSearchParams(majorArea = setOf(major))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"area.major\":\"$major\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses multiple major areas`() {
        // given
        val major1 = "Engenharias"
        val major2 = "Ciências Humanas"
        val params = ResearcherSearchParams(majorArea = setOf(major1, major2))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"area.major\":\"$major1\"},{\"area.major\":\"$major2\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses a single minor area`() {
        // given
        val minor = "Engenharia Civil"
        val params = ResearcherSearchParams(minorArea = setOf(minor))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"area.minors\":\"$minor\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses multiple minor areas`() {
        // given
        val minor1 = "Engenharia Civil"
        val minor2 = "Engenharia de Minas"
        val params = ResearcherSearchParams(minorArea = setOf(minor1, minor2))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"area.minors\":\"$minor1\"},{\"area.minors\":\"$minor2\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses campus`() {
        // given
        val campus = "Butantã"
        val params = ResearcherSearchParams(campus = campus)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"campus\":\"$campus\"}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses unity`() {
        // given
        val unity = "IME"
        val params = ResearcherSearchParams(unity = unity)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"unities\":\"$unity\"}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses bond`() {
        // given
        val bond = "Pesquisador"
        val params = ResearcherSearchParams(bond = bond)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"bond\":\"$bond\"}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses text term`() {
        // given
        val term = "Ágil"
        val params = ResearcherSearchParams(term = term)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$text\":{\"\$search\":\"$term\"}}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses a complex set of filters`() {
        // given
        val params = ResearcherSearchParams(
            campus = "Butantã",
            bond = "Pesquisador",
            unity = "IME",
            majorArea = setOf("Engenharias"),
            minorArea = setOf("Engenharia de Minas"),
            term = "Ágil"
        )

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = complexExpectation()
        assertEquals(expected, result)
    }

    private fun complexExpectation(): String = "{" +
        listOf(
            "\"\$or\":[{\"area.major\":\"Engenharias\"}]",
            "\"\$or\":[{\"area.minors\":\"Engenharia de Minas\"}]",
            "\"campus\":\"Butantã\"",
            "\"unities\":\"IME\"",
            "\"bond\":\"Pesquisador\"",
            "\"\$text\":{\"\$search\":\"Ágil\"}"
        ).joinToString(",") +
        "}"
}
