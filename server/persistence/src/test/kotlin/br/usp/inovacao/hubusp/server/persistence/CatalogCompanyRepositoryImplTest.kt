package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Address
import br.usp.inovacao.hubusp.server.catalog.Classification
import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import br.usp.inovacao.hubusp.server.persistence.models.CompanyAddress
import br.usp.inovacao.hubusp.server.persistence.models.CompanyClassification
import br.usp.inovacao.hubusp.server.persistence.models.CompanyModel
import br.usp.inovacao.hubusp.server.persistence.models.Partner
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

    @Test
    fun `it filters by unity`() {
        // given
        val unity = "FEA"
        val params = CompanySearchParams(unity = unity)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { unity in it.unities } }
    }

    @Test
    fun `it filters by single-token text`() {
        // given
        val term = "pluralidade"
        val params = CompanySearchParams(term = term)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.matches(term = term) } }
    }

    @Test
    fun `it reduces to a set of Ecosystems`() {
        // given
        cleanTestDb()
        seedDbForEcosystems()

        // when
        val result = underTest.getEcosystems()

        // then
        val expectedEcosystems = setOf("ABC", "BCD", "CDE", "DEF", "EFG")

        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { expectedEcosystems.contains(it) } }
    }

    @Test
    fun `it reduces to a set of Cities`() {
        // given
        cleanTestDb()
        seedDbForCities()

        // when
        val result = underTest.getCities()

        // then
        val expectedCities = setOf("São Paulo", "Caxias do Sul", "Pelotas")

        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { expectedCities.contains(it)} }
    }

    private fun seedDbForCities() {
        val coll = testDb.getCollection<CompanyModel>("companies")
        coll.insertMany(citiesSeeds())
    }

    private fun seedDbForEcosystems() {
        val coll = testDb.getCollection<CompanyModel>("companies")
        coll.insertMany(ecosystemsSeeds())
    }

    private fun citiesSeeds() = listOf(
        CompanyModel(
            address = CompanyAddress(
                cep = "05555-020",
                city = setOf("São Paulo"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = CompanyClassification(
                major = "Comércio e Serviços",
                minor = "Serviços Domésticos"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Microempresa"),
            description = "pluralidade foo bar baz",
            ecosystems = setOf("ABC"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            partners = setOf(
                Partner("Fulano", "1234567", "Aluno ou ex-aluno (graduação)",
                    "Faculdade de Economia, Administração e Contabilidade - FEA", "fulano@example.com", "(11) 98899-7654"),
                Partner("Ciclano", "98765432", "Aluno ou ex-aluno (graduação)" ,
                    "Instituto de Matemática e Estatística - IME", "cliclano@example.com", "(11) 98989-7676")
            )
        ),
        CompanyModel(
            address = CompanyAddress(
                cep = "05555-020",
                city = setOf("Caxias do Sul", "Pelotas"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = CompanyClassification(
                major = "Comércio e Serviços",
                minor = "Serviços Domésticos"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Microempresa"),
            description = "pluralidade foo bar baz",
            ecosystems = setOf("ABC"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            partners = setOf(
                Partner("Beltrano", "53726812", "Pesquisador",
                    "Faculdade de Arquitetura e Urbanismo - FAU", "beltrano@example.com", "(11) 98899-7654")
            )
        ),
    )

    private fun ecosystemsSeeds() = listOf(
        CompanyModel(
            address = CompanyAddress(
                cep = "05555-020",
                city = setOf("São Paulo"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = CompanyClassification(
                major = "Comércio e Serviços",
                minor = "Serviços Domésticos"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Microempresa"),
            description = "pluralidade foo bar baz",
            ecosystems = setOf("ABC"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            partners = setOf(
                Partner("Foobaz", "12345678", "Aluno Especial Pós Graduação",
                    "Faculdade de Economia, Administração e Contabilidade - FEA", "foobaz@example.com", "(11) 99999-8888"),
                Partner("Bazfoo", "87654321", "Aluno ou ex-aluno (pós-graduação)",
                    "Instituto de Matemática e Estatística - IME", "bazfoo@example.com", "(11) 99111-2121")
            )
        ),
        CompanyModel(
            address = CompanyAddress(
                cep = "05555-020",
                city = setOf("São Paulo"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = CompanyClassification(
                major = "Comércio e Serviços",
                minor = "Serviços Domésticos"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Microempresa"),
            description = "pluralidade foo bar baz",
            ecosystems = setOf("BCD", "CDE"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            partners = setOf(
                Partner("Nome", "9203746", "Aluno ou ex-aluno (graduação)",
                    "Faculdade de Arquitetura e Urbanismo - FAU", "nome@example.com", "(11) 99444-3535")
            )
        ),
        CompanyModel(
            address = CompanyAddress(
                cep = "05555-020",
                city = setOf("São Paulo"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = CompanyClassification(
                major = "Comércio e Serviços",
                minor = "Serviços Domésticos"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Microempresa"),
            description = "pluralidade foo bar baz",
            ecosystems = setOf("CDE", "DEF", "EFG"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            partners = setOf(
                Partner("Pessoa","1029384", "Pesquisador",
                    "Instituto de Física - IF", "pessoa@example.com", "(11) 99595-9494"),
                Partner("Ser humano", "94857632", "Aluno ou ex-aluno (graduação)",
                    "Instituto de Química - IQ", "ser_humano@example.com", "(11) 99234-5678"),
                Partner("Gente", "10293846", "Aluno ou ex-aluno (pós-graduação)",
                    "Instituto de Pesquisa e Tecnologia - IPT", "gente@example.com", "(11) 99080-5070")
            )
        ),
    )

    private fun cleanTestDb() {
        val companyCollection = testDb.getCollection<CompanyModel>("companies")
        companyCollection.deleteMany("{}")
    }

    private fun seedTestDb() {
        val companyCollection = testDb.getCollection<CompanyModel>("companies")
        companyCollection.insertMany(testSeeds())
    }

    private fun testSeeds() = listOf(
        CompanyModel(
            address = CompanyAddress(
                cep = "05555-020",
                city = setOf("São Paulo"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = CompanyClassification(
                major = "Comércio e Serviços",
                minor = "Serviços Domésticos"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Microempresa"),
            description = "pluralidade foo bar baz",
            ecosystems = setOf("InovaLab@POLI"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            partners = setOf(
                Partner("Fulano", "1234567", "Aluno ou ex-aluno (graduação)",
                    "FEA", "fulano@example.com", "(11) 98899-7654"),
                Partner("Ciclano", "98765432", "Aluno ou ex-aluno (graduação)" ,
                    "IME", "cliclano@example.com", "(11) 98989-7676")
            )
        ),
        CompanyModel(
            address = CompanyAddress(
                cep = "05555-020",
                city = setOf("Rio de Janeiro"),
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            classification = CompanyClassification(
                major = "Infraestrutura e Construção",
                minor = "Construção"
            ),
            cnae = "042.0230-02",
            companySize = setOf("Grande Empresa"),
            description = "foo bar baz",
            ecosystems = setOf("Habits"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = emptySet(),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            partners = setOf(
                Partner("Foobaz", "12345678", "Aluno Especial Pós Graduação",
                    "FEA", "foobaz@example.com", "(11) 99999-8888"),
                )
        )
    )
}
