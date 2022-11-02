package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import kotlin.test.Test
import kotlin.test.assertEquals

class CompanyExtensionKtTest {
    @Test
    fun `it parses a single major`() {
        // given
        val major = "Infraestrutura e Construção"
        val params = CompanySearchParams(areaMajors = setOf(major))

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"\$or\":[{\"classification.major\":\"$major\"}]}"
        assertEquals(handmade, result)
    }

    @Test
    fun `it parses multiple majors`() {
        // given
        val major1 = "Infraestrutura e Construção"
        val major2 = "Comércio e Serviços"
        val params = CompanySearchParams(areaMajors = setOf(major1, major2))

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"\$or\":[{\"classification.major\":\"$major1\"},{\"classification.major\":\"$major2\"}]}"
        assertEquals(handmade, result)
    }

    @Test
    fun `it parses a single minor`() {
        // given
        val minor = "Construção"
        val params = CompanySearchParams(areaMinors = setOf(minor))

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"\$or\":[{\"classification.minor\":\"$minor\"}]}"
        assertEquals(handmade, result)
    }

    @Test
    fun `it parses multiple minors`() {
        // given
        val minor1 = "Construção"
        val minor2 = "Serviços Domésticos"
        val params = CompanySearchParams(areaMinors = setOf(minor1, minor2))

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"\$or\":[{\"classification.minor\":\"$minor1\"},{\"classification.minor\":\"$minor2\"}]}"
        assertEquals(handmade, result)
    }

    @Test
    fun `it parses city`() {
        // given
        val city = "São Paulo"
        val params = CompanySearchParams(city = city)

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"address.city\":\"$city\"}"
        assertEquals(handmade, result)
    }

    @Test
    fun `it parses ecosystem`() {
        // given
        val ecosystem = "InovaLab@POLI"
        val params = CompanySearchParams(ecosystem = ecosystem)

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"ecosystems\":\"$ecosystem\"}"
        assertEquals(handmade, result)
    }

    @Test
    fun `it parses size`() {
        // given
        val size = "Grande Empresa"
        val params = CompanySearchParams(size = size)

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"companySize\":\"$size\"}"
        assertEquals(handmade, result)
    }

    @Test
    fun `it parses text term`() {
        // given
        val term = "pluralidade"
        val params = CompanySearchParams(term = term)

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"\$text\":{\"\$search\":\"$term\"}}"
        assertEquals(handmade, result)
    }

    @Test
    fun `it parses unity`() {
        // given
        val unity = "FEA"
        val params = CompanySearchParams(unity = unity)

        // when
        val result = params.toCollectionFilter()

        // then
        val handmade = "{\"partners.unity\":\"$unity\"}"
        assertEquals(handmade, result)
    }
}
