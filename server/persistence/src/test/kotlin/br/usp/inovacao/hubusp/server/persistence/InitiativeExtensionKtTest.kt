package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.InitiativeSearchParams
import kotlin.test.Test
import kotlin.test.assertEquals

internal class InitiativeExtensionKtTest {
    @Test
    fun `it parses a single classification`() {
        // given
        val classification = "Agente Institucional"
        val params = InitiativeSearchParams(classifications = setOf(classification))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"classification\":\"$classification\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses multiple classifications`() {
        // given
        val classification1 = "Agente Institucional"
        val classification2 = "Entidade Estudantil"
        val params = InitiativeSearchParams(classifications = setOf(classification1, classification2))

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"\$or\":[{\"classification\":\"$classification1\"},{\"classification\":\"$classification2\"}]}"
        assertEquals(expected, result)
    }

    @Test
    fun `it parses a campus`() {
        // given
        val campus = "Butantã"
        val params = InitiativeSearchParams(campus = campus)

        // when
        val result = params.toCollectionFilter()

        // then
        val expected = "{\"localization\":\"$campus\"}"
        assertEquals(expected, result)
    }
}