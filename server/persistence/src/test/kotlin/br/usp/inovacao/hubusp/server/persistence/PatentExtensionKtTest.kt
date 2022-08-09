package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PatentSearchParams
import kotlin.test.Test
import kotlin.test.assertEquals

internal class PatentExtensionKtTest {
    @Test
    fun `it parses a single major`() {
        // given
        val major = "A - Necessidades Humanas"
        val params = PatentSearchParams(majorAreas = setOf(major))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"classification.primary.cip\":\"$major\"},{\"classification.secondary.cip\":\"$major\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses multiple majors`() {
        // given
        val major1 = "A - Necessidades Humanas"
        val major2 = "G - Física"
        val params = PatentSearchParams(majorAreas = setOf(major1, major2))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"classification.primary.cip\":\"$major1\"},{\"classification.secondary.cip\":\"$major1\"},{\"classification.primary.cip\":\"$major2\"},{\"classification.secondary.cip\":\"$major2\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses a single minor`() {
        // given
        val minor = "C07 - Química orgânica"
        val params = PatentSearchParams(minorAreas = setOf(minor))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"classification.primary.subarea\":\"$minor\"},{\"classification.secondary.subarea\":\"$minor\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses multiple minors`() {
        // given
        val minor1 = "A61 - Ciência médica ou veterinária; higiene"
        val minor2 = "G01 - Medição; teste"
        val params = PatentSearchParams(minorAreas = setOf(minor1, minor2))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"classification.primary.subarea\":\"$minor1\"},{\"classification.secondary.subarea\":\"$minor1\"},{\"classification.primary.subarea\":\"$minor2\"},{\"classification.secondary.subarea\":\"$minor2\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses a status`() {
        // given
        val status = "Concedida"
        val params = PatentSearchParams(status = status)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"status\":\"$status\"}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses text term`() {
        // given
        val term = "partículas"
        val params = PatentSearchParams(term = term)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$text\":{\"\$search\":\"$term\"}}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses all fields`() {
        // given
        val major1 = "A - Necessidades Humanas"
        val major2 = "G - Física"
        val minor1 = "A61 - Ciência médica ou veterinária; higiene"
        val minor2 = "G01 - Medição; teste"
        val status = "Concedida"
        val term = "partículas"
        val params = PatentSearchParams(
            majorAreas = setOf(major1, major2),
            minorAreas = setOf(minor1, minor2),
            status = status,
            term = term,
        )

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"classification.primary.cip\":\"$major1\"},{\"classification.secondary.cip\":\"$major1\"},{\"classification.primary.cip\":\"$major2\"},{\"classification.secondary.cip\":\"$major2\"}]," +
                "\"\$or\":[{\"classification.primary.subarea\":\"$minor1\"},{\"classification.secondary.subarea\":\"$minor1\"},{\"classification.primary.subarea\":\"$minor2\"},{\"classification.secondary.subarea\":\"$minor2\"}]," +
                "\"status\":\"$status\",\"\$text\":{\"\$search\":\"$term\"}}"
        assertEquals(expected, result)
    }
}