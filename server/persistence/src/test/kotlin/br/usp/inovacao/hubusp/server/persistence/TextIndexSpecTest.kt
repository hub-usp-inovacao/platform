package br.usp.inovacao.hubusp.server.persistence

import org.bson.Document
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TextIndexSpecTest {
    @Test
    fun `it builds text index keys`() {
        val spec = TextIndexSpec(
            collectionName = "companies",
            textFields = listOf("name", "description"),
            ascendingFields = listOf("active"),
        )

        val keys = spec.keys()

        assertEquals("text", keys.getString("name"))
        assertEquals("text", keys.getString("description"))
        assertEquals(1, keys.getInteger("active"))
    }

    @Test
    fun `it builds text index options`() {
        val spec = TextIndexSpec(
            collectionName = "companies",
            textFields = listOf("name"),
        )

        val options = spec.options()

        assertEquals("companies_fts_pt_v1", options.name)
        assertEquals(TEXT_INDEX_LANGUAGE, options.defaultLanguage)
    }

    @Test
    fun `it exposes the expected index specs`() {
        assertTrue(TEXT_INDEX_SPECS.any { it.collectionName == "companies" })
        assertTrue(TEXT_INDEX_SPECS.any { it.collectionName == "pdis" })
        assertTrue(TEXT_INDEX_SPECS.any { it.collectionName == "patents" })
    }
}
