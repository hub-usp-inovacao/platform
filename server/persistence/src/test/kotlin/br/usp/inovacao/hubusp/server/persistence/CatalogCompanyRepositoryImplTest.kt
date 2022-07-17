package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Address
import br.usp.inovacao.hubusp.server.catalog.Classification
import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.deleteMany
import org.litote.kmongo.getCollection
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class CatalogCompanyRepositoryImplTest() {

    private lateinit var testDb: MongoDatabase
    private lateinit var underTest: CatalogCompanyRepositoryImpl

    @BeforeTest
    fun setup() {
        testDb = connectToTestDb()
        underTest = CatalogCompanyRepositoryImpl(testDb)
        seedTestDb()
    }

    @AfterTest
    fun teardown() {
        cleanTestDb()
    }

    @Test
    fun `it filters by major`() {
        // given
        val major = "Infraestrutura e Construção"
        val params = CompanySearchParams(areaMajors = setOf(major))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.classification.major == major } }
    }

    @Test
    fun `it filters by minor`() {
        // given
        val minor = "Construção"
        val params = CompanySearchParams(areaMinors = setOf(minor))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.classification.minor == minor } }
    }

    @Test
    fun `it filters by city`() {
        // given
        val city = "São Paulo"
        val params = CompanySearchParams(city = city)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { city in it.address.city } }
    }

    @Test
    fun `it filters by ecosystem`() {
        // given
        val ecosystem = "InovaLab@POLI"
        val params = CompanySearchParams(ecosystem = ecosystem)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { ecosystem in it.ecosystems } }
    }

    @Test
    fun `it filters by size`() {
        // given
        val size = "Microempresa"
        val params = CompanySearchParams(size = size)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { size in it.companySize } }
    }

    private fun cleanTestDb() {
        val companyCollection = testDb.getCollection<Company>("companies")
        companyCollection.deleteMany("{}")
    }

    private fun seedTestDb() {
        val companyCollection = testDb.getCollection<Company>("companies")
        companyCollection.insertMany(testSeeds())
    }

    private fun testSeeds() = listOf(
        Company(
            address = Address(
                cep = "05555-020",
                city = setOf("São Paulo"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = Classification(
                major = "Comércio e Serviços",
                minor = "Serviços Domésticos"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Microempresa"),
            description = "foo bar baz",
            ecosystems = setOf("InovaLab@POLI"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br"
        ),
        Company(
            address = Address(
                cep = "05555-020",
                city = setOf("Rio de Janeiro"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = Classification(
                major = "Infraestrutura e Construção",
                minor = "Construção"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Grande Empresa"),
            description = "foo bar baz",
            ecosystems = setOf("InovaLab@POLI"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br"
        )
    )
}