package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Category
import br.usp.inovacao.hubusp.server.catalog.Discipline
import br.usp.inovacao.hubusp.server.catalog.DisciplineSearchParams
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
    fun `it filters by category`() {
        // given
        val params = DisciplineSearchParams(categories = setOf("Negócios","Empreendedorismo"))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.all { it.category.business || it.category.entrepreneurship } }
    }

    private fun seedTestDb() {
        val disciplineCollection = testDb.getCollection<Discipline>("disciplines")
        disciplineCollection.insertMany(testSeeds())
    }

    private fun cleanTestDb() {
        val disciplineCollection = testDb.getCollection<Discipline>("disciplines")
        disciplineCollection.deleteMany("{}")
    }

    private fun testSeeds() = listOf(
        Discipline(
            name = "ABC0001",
            unity = "ABC",
            campus = "Butantã",
            level = "Quero aprender!",
            nature = "Graduação",
            category = Category(
                innovation = false,
                entrepreneurship = false,
                business = false,
                intellectual_property = true
            )
        ),
        Discipline(
            name = "ABC0004",
            unity = "ABC",
            campus = "Butantã",
            level = "Quero aprender!",
            nature = "Pós-graduação",
            category = Category(
                innovation = false,
                entrepreneurship = true,
                business = false,
                intellectual_property = true
            )
        ),
        Discipline(
            name = "ABC0002",
            unity = "ABC",
            campus = "Butantã",
            level = "Quero aprender!",
            nature = "Pós-graduação",
            category = Category(
                innovation = false,
                entrepreneurship = false,
                business = false,
                intellectual_property = true
            )
        ),
        Discipline(
            name = "ABC0003",
            unity = "BCD",
            campus = "São Carlos",
            level = "Tenho uma ideia, e agora?",
            nature = "Pós-graduação",
            category = Category(
                innovation = false,
                entrepreneurship = false,
                business = true,
                intellectual_property = true
            )
        )
    )
}
