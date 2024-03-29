package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.KnowledgeAreas
import br.usp.inovacao.hubusp.server.catalog.Researcher
import br.usp.inovacao.hubusp.server.catalog.ResearcherSearchParams
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.deleteMany
import org.litote.kmongo.getCollection
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

internal class CatalogResearcherRepositoryImplTest {
    private lateinit var testDb: MongoDatabase
    private lateinit var underTest: CatalogResearcherRepositoryImpl

    @BeforeTest
    fun setup() {
        testDb = connectToTestDb()
        underTest = CatalogResearcherRepositoryImpl(testDb)
        seedTestDb()
    }

    @AfterTest
    fun teardown() {
        cleanTestDb()
    }

    @Test
    fun `it filters by major area`() {
        // given
        val major = "Ciências Exatas e da Terra"
        val params = ResearcherSearchParams(majorArea = setOf(major))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { major in it.area.major } }
    }

    @Test
    fun `it filters by minor`() {
        // given
        val minor = "Ciência da Computação"
        val params = ResearcherSearchParams(minorArea = setOf(minor))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { minor in it.area.minors } }
    }

    @Test
    fun `it filters by campus`() {
        // given
        val campus = "USP Leste"
        val params = ResearcherSearchParams(campus = campus)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.campus == campus } }
    }

    @Test
    fun `it filters by unity`() {
        // given
        val unity = "IME"
        val params = ResearcherSearchParams(unity = unity)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { unity in it.unities } }
    }

    @Test
    fun `it filters by bond`() {
        // given
        val bond = "Docente Sênior"
        val params = ResearcherSearchParams(bond = bond)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.bond == bond } }
    }

    @Test
    fun `it filters by text search`() {
        // given
        val term = "Ágil"
        val params = ResearcherSearchParams(term = term)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all {
            it.name.contains(term) ||
                    it.skills.any { skill -> skill.contains(term) } ||
                    it.equipments.any { equip -> equip.contains(term) } ||
                    it.services.any { service -> service.contains(term) } ||
                    it.keywords.any { word -> word.contains(term) }
        } }
    }

    private fun seedTestDb() {
        val researcherCollection = testDb.getCollection<Researcher>("skills")
        researcherCollection.insertMany(testSeeds())
    }

    private fun cleanTestDb() {
        val researcherCollection = testDb.getCollection<Researcher>("skills")
        researcherCollection.deleteMany("{}")
    }

    private fun testSeeds() = listOf(
        Researcher(
            name = "Fulano de Tal",
            email = "fulano@usp.br",
            unities = setOf("IME"),
            campus = "Butantã",
            skills = setOf("testes automatizados", "Desenvolvimento Ágil"),
            equipments = emptySet(),
            services = emptySet(),
            area = KnowledgeAreas(
                major = setOf("Ciências Exatas e da Terra"),
                minors = setOf("Ciência da Computação")
            ),
            keywords = setOf("testes automatizados"),
            lattes = "http://lattes.cnpq.org.br/130981230981023890812",
            photo = "N/D",
            phone = "N/D",
            bond = "Docente"
        ),
        Researcher(
            name = "Beltrano de Tal",
            email = "fulano@usp.br",
            unities = setOf("ABC"),
            campus = "USP Leste",
            skills = setOf("testes automatizados"),
            equipments = emptySet(),
            services = emptySet(),
            area = KnowledgeAreas(
                major = setOf("Ciências Humanas"),
                minors = setOf("Filosofia")
            ),
            keywords = setOf("testes automatizados"),
            lattes = "http://lattes.cnpq.org.br/130981230981023890812",
            photo = "N/D",
            phone = "N/D",
            bond = "Docente Sênior"
        )
    )
}
