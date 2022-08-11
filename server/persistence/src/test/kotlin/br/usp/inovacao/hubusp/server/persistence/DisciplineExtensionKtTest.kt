package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.DisciplineSearchParams
import org.junit.Test
import kotlin.test.assertEquals

internal class DisciplineExtensionKtTest {
    @Test
    fun `it handles correctly with params with single category`() {
        // given
        val category = "Inovação"
        val params = DisciplineSearchParams(categories = setOf(category))

        // when
        val dqfJson = params.toCollectionFilter()

        // then
        val handmadeJson = "{\"\$or\":[{\"category.innovation\":true}]}"
        assertEquals(handmadeJson, dqfJson)
    }

    @Test
    fun `it handles correctly with params with multiple categories`() {
        // given
        val params = DisciplineSearchParams(categories = setOf("Inovação", "Negócios"))

        // when
        val dqfJson = params.toCollectionFilter()

        // then
        val handmadeJson = "{\"\$or\":[{\"category.business\":true},{\"category.innovation\":true}]}"
        assertEquals(handmadeJson, dqfJson)
    }

    @Test
    fun `it handles text term`() {
        // given
        val term = "utilidade"
        val params = DisciplineSearchParams(term = term)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$text\":{\"\$search\":\"$term\"}}"
        assertEquals(expected, result)
    }

    @Test
    fun `it works correctly with complex params`() {
        // given
        val params = DisciplineSearchParams(
            categories = setOf("Negócios","Inovação"),
            unity = "somewhere"
        )

        // when
        val dqfJson = params.toCollectionFilter()

        // then
        val handmadeJson = "{\"\$or\":[{\"category.business\":true},{\"category.innovation\":true}],\"unity\":\"somewhere\"}"
        assertEquals(handmadeJson, dqfJson)
    }
}
