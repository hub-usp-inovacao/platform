package br.usp.inovacao.hubusp.catalog.domain

import kotlin.test.Test
import kotlin.test.assertEquals

internal class DisciplineQueryFilterTest {
    @Test
    fun `it handles correctly with params with single category`() {
        // given
        val category = "single"
        val params = mapOf("categories" to listOf(category))

        // when
        val dqfJson = DisciplineQueryFilter.from(params).toJson()

        // then
        val handmadeJson = "{\"categories\":{\"\$in\":[\"$category\"]}}"
        assertEquals(handmadeJson, dqfJson)
    }

    @Test
    fun `it handles correctly with params with multiple categories`() {
        // given
        val params = mapOf("categories" to listOf("multiple,categories"))

        // when
        val dqfJson = DisciplineQueryFilter.from(params).toJson()

        // then
        val handmadeJson = "{\"categories\":{\"\$in\":[\"multiple\",\"categories\"]}}"
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
            "categories" to listOf("two,things"),
            "unity" to listOf("somewhere")
        )

        // when
        val dqfJson = DisciplineQueryFilter.from(params).toJson()

        // then
        val handmadeJson = "{\"categories\":{\"\$in\":[\"two\",\"things\"]},\"unity\":\"somewhere\"}"
        assertEquals(handmadeJson, dqfJson)
    }
}
