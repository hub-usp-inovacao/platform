package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Discipline
import br.usp.inovacao.hubusp.server.catalog.Category
import br.usp.inovacao.hubusp.server.catalog.DisciplineSearchParams
import br.usp.inovacao.hubusp.server.persistence.models.*
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.deleteMany
import org.litote.kmongo.getCollection
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertTrue

internal class CatalogDisciplineRepositoryImplTest {
    private lateinit var testDb: MongoDatabase
    private lateinit var underTest: CatalogDisciplineRepositoryImpl

    @BeforeTest
    fun setup() {
        testDb = connectToTestDb()
        underTest = CatalogDisciplineRepositoryImpl(testDb)
        seedTestDb()
    }

    @AfterTest
    fun teardown() {
        cleanTestDb()
    }

    @Test
    fun `it works when filter is empty`() {
        // given
        val params = DisciplineSearchParams()

        // when
        val result = underTest.filter(params)

        // then
        assertIs<Set<Discipline>>(result)
    }

    @Test
    fun `it filters by campus`() {
        // given
        val givenCampus = "São Carlos"
        val params = DisciplineSearchParams(campus = givenCampus)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.all { it.campus == givenCampus } }
    }

    @Test
    fun `it filters by unity`() {
        // given
        val givenUnity = "BCD"
        val params = DisciplineSearchParams(unity = givenUnity)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.all { it.unity == givenUnity } }
    }

    @Test
    fun `it filters by level`() {
        // given
        val givenLevel = "Tenho uma ideia, e agora?"
        val params = DisciplineSearchParams(level = givenLevel)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.all { it.level == givenLevel } }
    }

    @Test
    fun `it filters by nature`() {
        // given
        val givenNature = "Graduação"
        val params = DisciplineSearchParams(nature = givenNature)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.all { it.nature == givenNature } }
    }

    @Test
    fun `it filters by offering period`() {
        // given
        val givenPeriod = "1º sem. 2023"
        val params = DisciplineSearchParams(offeringPeriod = givenPeriod)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.all { it.offeringPeriod == givenPeriod } }
    }

    @Test
    fun `it filters by category`() {
        // given
        val params = DisciplineSearchParams(categories = setOf("Negócios","Empreendedorismo"))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.all { it.category.business || it.category.entrepreneurship } }
    }

    @Test
    fun `it filters by single-token text term`() {
        // given
        val term = "utilidade"
        val params = DisciplineSearchParams(term = term)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.name.contains(term) } }
    }

    @Test
    fun `it converts DisciplineModel into Discipline`() {
        // given
        val discipline = getDisciplineModel()

        // when
        val result = discipline.toCatalogDiscipline()

        // then
        assertIs<Discipline>(result)
    }

    private fun getDisciplineModel() =  DisciplineModel(
        campus = "USP Leste",
        category = DisciplineCategory(
            innovation = false,
            entrepreneurship = false,
            business = false,
            intellectual_property = true
        ),
        description = "Analisar as contribuições da inovação para o desenvolvimento do setor de serviços, ...",
        keywords = setOf("foo", "baz"),
        level = "Quero aprender!",
        name = "ACH1575",
        nature = "Graduação",
        offeringPeriod = "N/D",
        start_date = "",
        unity = "Escola de Artes, Ciências e Humanidades - EACH",
        url = "https://uspdigital.usp.br/jupiterweb/obterDisciplina?sgldis=ACH2008&nomdis="
    )

    private fun seedTestDb() {
        val disciplineCollection = testDb.getCollection<DisciplineModel>("disciplines")
        disciplineCollection.insertMany(testSeeds())
    }

    private fun cleanTestDb() {
        val disciplineCollection = testDb.getCollection<DisciplineModel>("disciplines")
        disciplineCollection.deleteMany("{}")
    }

    private fun testSeeds() = listOf(
        DisciplineModel(
            campus = "Butantã",
            level = "Quero aprender!",
            name = "ABC0001",
            nature = "Graduação",
            unity = "ABC",
            category = DisciplineCategory(
                innovation = false,
                entrepreneurship = false,
                business = false,
                intellectual_property = true
            ),
            description = "",
            keywords = setOf("foo", "baz"),
            offeringPeriod = "1º sem. 2023",
            start_date = "",
            url = ""
        ),
        DisciplineModel(
            name = "ABC0004",
            unity = "ABC",
            campus = "Butantã",
            level = "Quero aprender!",
            nature = "Pós-graduação",
            category = DisciplineCategory(
                innovation = false,
                entrepreneurship = true,
                business = false,
                intellectual_property = true
            ),
            description = "",
            keywords = setOf("foo", "baz"),
            offeringPeriod = "N/D",
            start_date = "",
            url = ""
        ),
        DisciplineModel(
            name = "ABC0002",
            unity = "ABC",
            campus = "Butantã",
            level = "Quero aprender!",
            nature = "Pós-graduação",
            category = DisciplineCategory(
                innovation = false,
                entrepreneurship = false,
                business = false,
                intellectual_property = true
            ),
            description = "",
            keywords = setOf("foo", "baz"),
            offeringPeriod = "N/D",
            start_date = "",
            url = ""
        ),
        DisciplineModel(
            name = "ABC0003 utilidade",
            unity = "BCD",
            campus = "São Carlos",
            level = "Tenho uma ideia, e agora?",
            nature = "Pós-graduação",
            category = DisciplineCategory(
                innovation = false,
                entrepreneurship = false,
                business = true,
                intellectual_property = true
            ),
            description = "",
            keywords = setOf("foo", "baz"),
            offeringPeriod = "2º sem. 2022",
            start_date = "",
            url = ""
        )
    )
}
