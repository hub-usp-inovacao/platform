package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PDISearchParams
import kotlin.test.Test
import kotlin.test.assertEquals

class PDIExtensionKtTest {
    @Test
    fun `it parses a single category`() {
        // given
        val category = "Centro de Pesquisa em Engenharia"
        val params = PDISearchParams(categories = setOf(category))

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"\$or\":[{\"category\":\"$category\"}]}"
        assertEquals(result, handmade)
    }

    @Test
    fun `it parses multiple categories`() {
        // given
        val category1 = "NAP"
        val category2 = "Centro de Pesquisa em Engenharia"
        val params = PDISearchParams(categories = setOf(category1, category2))

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"\$or\":[{\"category\":\"$category1\"},{\"category\":\"$category2\"}]}"
        assertEquals(result, handmade)
    }

    @Test
    fun `it parses campus`() {
        // given
        val campus = "Butantã"
        val params = PDISearchParams(campus = campus)

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"campus\":\"$campus\"}"
        assertEquals(result, handmade)
    }

    @Test
    fun `it parses text term`() {
        // given
        val term = "dispositivos"
        val params = PDISearchParams(term = term)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$text\":{\"\$search\":\"$term\"}}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses all fields`() {
        // given
        val category1 = "NAP"
        val category2 = "Centro de Pesquisa em Engenharia"
        val campus = "Butantã"
        val term = "dispositivos"
        val params = PDISearchParams(
            categories = setOf(category1, category2),
            campus = campus,
            term = term,
        )

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"\$or\":[{\"category\":\"$category1\"},{\"category\":\"$category2\"}],\"campus\":\"$campus\",\"\$text\":{\"\$search\":\"$term\"}}"
        assertEquals(result, handmade)
    }
}