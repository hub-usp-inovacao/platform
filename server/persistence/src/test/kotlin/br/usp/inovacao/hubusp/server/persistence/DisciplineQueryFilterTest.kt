package br.usp.inovacao.hubusp.server.persistence

import org.junit.Test
import kotlin.test.assertEquals

internal class DisciplineQueryFilterTest {
    @Test
    fun `it handles correctly with params with single category`() {
        // given
        val category = "Inovação"
        val params = mapOf("categories" to listOf(category))

        // when
        val dqfJson = DisciplineQueryFilter.from(params).toJson()

        // then
        val handmadeJson = "{\"\$or\":[{\"category.innovation\":true}]}"
        assertEquals(handmadeJson, dqfJson)
    }

    @Test
    fun `it handles correctly with params with multiple categories`() {
        // given
        val params = mapOf("categories" to listOf("Inovação,Negócios"))

        // when
        val dqfJson = DisciplineQueryFilter.from(params).toJson()

        // then
        val handmadeJson = "{\"\$or\":[{\"category.business\":true},{\"category.innovation\":true}]}"
        assertEquals(handmadeJson, dqfJson)
    }

    @Test
    fun `it handle correctly with params with simple keys`() {
        // given
        val simpleKeys = listOf("unity", "campus", "level", "nature")
        val randomKey = simpleKeys.random()
        val params = mapOf(randomKey to listOf("anything"))

        // when
        val dqfJson = DisciplineQueryFilter.from(params).toJson()

        // then
        val handmadeJson = """{"$randomKey":"anything"}"""
        assertEquals(handmadeJson, dqfJson)
    }

    @Test
    fun `it works correctly with complex params`() {
        // given
        val params = mapOf(
            "categories" to listOf("Negócios,Inovação"),
            "unity" to listOf("somewhere")
        )

        // when
        val dqfJson = DisciplineQueryFilter.from(params).toJson()

        // then
        val handmadeJson = "{\"unity\":\"somewhere\",\"\$or\":[{\"category.business\":true},{\"category.innovation\":true}]}"
        assertEquals(handmadeJson, dqfJson)
    }
}
