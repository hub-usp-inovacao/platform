package br.usp.inovacao.hubusp.curatorship.sheets.utils

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HelperTest {
    @Test
    fun `it converts indexes to spreadsheet letters`() {
        assertEquals("A", indexToColumnLetter(0))
        assertEquals("Z", indexToColumnLetter(25))
        assertEquals("AA", indexToColumnLetter(26))
        assertEquals("AZ", indexToColumnLetter(51))
    }

    @Test
    fun `it formats urls`() {
        assertNull(formatUrl(null))
        assertNull(formatUrl(""))
        assertNull(formatUrl("N/D"))
        assertEquals("https://example.com", formatUrl("example.com"))
        assertEquals("http://example.com", formatUrl("http://example.com"))
    }

    @Test
    fun `it handles N D values`() {
        assertEquals(emptySet(), splitUnlessND(null))
        assertEquals(emptySet(), splitUnlessND("N/D"))
        assertEquals(setOf("a", "b"), splitUnlessND("a; b"))
        assertEquals("N/D", handleND(null))
        assertEquals("foo", handleND("foo"))
    }

    @Test
    fun `it formats photos and splits strings`() {
        assertNull(formatPhoto(null))
        assertNull(formatPhoto("N/D"))
        assertEquals("https://drive.google.com/thumbnail?id=abc123", formatPhoto("abc123"))
        assertEquals("https://example.com/photo.png", formatPhoto("https://example.com/photo.png"))

        assertEquals(setOf("a", "b"), splitAndTrim("a, b", ','))
    }
}
