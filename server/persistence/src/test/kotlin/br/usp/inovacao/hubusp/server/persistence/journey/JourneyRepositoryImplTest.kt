package br.usp.inovacao.hubusp.server.persistence.journey

import br.usp.inovacao.hubusp.server.discovery.ImproveStepFilter
import br.usp.inovacao.hubusp.server.discovery.JourneyStep
import br.usp.inovacao.hubusp.server.discovery.LearnStepFilters
import br.usp.inovacao.hubusp.server.discovery.PracticeStepFilter
import br.usp.inovacao.hubusp.server.persistence.connectToTestDb
import br.usp.inovacao.hubusp.server.persistence.models.*
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.deleteMany
import org.litote.kmongo.getCollection
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class JourneyRepositoryImplTest {

    private lateinit var underTest: JourneyRepositoryImpl
    private lateinit var testDB: MongoDatabase

    @BeforeTest
    fun setup() {
        testDB = connectToTestDb()
        underTest = JourneyRepositoryImpl(testDB)
    }

    @Test
    fun `it finds all records in Learn with no filter`() = learnTest {
        // given
        val filter = LearnStepFilters()

        // when
        val result = underTest.find(JourneyStep.Learn, filter)

        // then
        assertEquals(learnSeeds().size, result.size)
    }

    @Test
    fun `it filters in Learn by nature`() = learnTest {
        // given
        val filter = LearnStepFilters(nature = "Pós-graduação")

        // when
        val result = underTest.find(JourneyStep.Learn, filter)

        // then
        assertEquals(1, result.size)
    }

    @Test
    fun `it filters in Learn by level`() = learnTest {
        // given
        val filter = LearnStepFilters(level = "Preciso testar minha ideia!")

        // when
        val result = underTest.find(JourneyStep.Learn, filter)

        // then
        assertEquals(1, result.size)
    }

    @Test
    fun `it finds all records in Practice with no filter`() = practiceTest {
        // given
        val filter = PracticeStepFilter()

        // when
        val result = underTest.find(JourneyStep.Practice, filter)

        // then
        assertEquals(practiceSeeds().size, result.size)
    }

    @Test
    fun `it filters in Practice by category`() = practiceTest {
        // given
        val filter = PracticeStepFilter(category = "Entidade Estudantil")

        // when
        val result = underTest.find(JourneyStep.Practice, filter)

        // then
        assertEquals(1, result.size)
    }

    @Test
    fun `it finds all records in Improve with no filter`() = improveTest {
        // given
        val filter = ImproveStepFilter()

        // when
        val result = underTest.find(JourneyStep.Improve, filter)

        // then
        assertEquals(improveSeeds().size, result.size)
    }

    @Test
    fun `it filters in Improve by category`() = improveTest {
        // given
        val filter = ImproveStepFilter(category = "CEPID")

        // when
        val result = underTest.find(JourneyStep.Improve, filter)

        // then
        assertEquals(1, result.size)
    }

    private fun learnTest(block: () -> Unit) {
        seedTestDbForLearnStep()
        block()
        cleanTestDbAfterLearn()
    }

    private fun practiceTest(block: () -> Unit) {
        seedTestDbForPracticeStep()
        block()
        cleanTestDbAfterPractice()
    }

    private fun improveTest(block: () -> Unit) {
        seedTestDbForImproveStep()
        block()
        cleanTestDbAfterImprove()
    }

    private fun cleanTestDbAfterImprove() {
        val collection = testDB.getCollection<PDIModel>("pdis")
        collection.deleteMany("{}")
    }

    private fun seedTestDbForImproveStep() {
        val collection = testDB.getCollection<PDIModel>("pdis")
        collection.insertMany(improveSeeds())
    }

    private fun improveSeeds() = listOf(
        PDIModel(
            category = "CEPID",
            name = "Foo de Bar baz",
            campus = "Butantã",
            unity = "IME",
            coordinator = "Fulano",
            site = "https://www.ime.usp.br",
            email = "",
            phone = "",
            description = "",
            tags = emptySet()
        ),
        PDIModel(
            category = "INCT",
            name = "Bar de Baz Foo",
            campus = "Butantã",
            unity = "IME",
            coordinator = "Fulano",
            site = "https://www.foo.usp.br",
            email = "",
            phone = "",
            description = "",
            tags = emptySet()
        )
    )

    private fun seedTestDbForPracticeStep() {
        val collection = testDB.getCollection<InitiativeModel>("initiatives")
        collection.insertMany(practiceSeeds())
    }

    private fun cleanTestDbAfterPractice() {
        val collection = testDB.getCollection<InitiativeModel>("initiatives")
        collection.deleteMany("{}")
    }

    private fun practiceSeeds() = listOf(
        InitiativeModel(
            classification = "Empresa Jr.",
            contact = InitiativeContact("", ""),
            description = "",
            email = null,
            localization = "",
            name = "Foo de Bar baz",
            tags = setOf("foo", "baz"),
            unity = "IME",
            url = "https://www.ime.usp.br"
        ),
        InitiativeModel(
            classification = "Entidade Estudantil",
            contact = InitiativeContact("", ""),
            description = "",
            email = null,
            localization = "",
            name = "Bar de Baz Foo",
            tags = setOf("foo", "baz"),
            unity = "Poli",
            url = "https://www.poli.usp.br"
        )
    )

    private fun cleanTestDbAfterLearn() {
        val collection = testDB.getCollection<DisciplineModel>("disciplines")
        collection.deleteMany("{}")
    }

    private fun seedTestDbForLearnStep() {
        val collection = testDB.getCollection<DisciplineModel>("disciplines")
        collection.insertMany(learnSeeds())
    }

    private fun learnSeeds() = listOf(
        DisciplineModel(
            campus = "Butantã",
            category = DisciplineCategory(true, true, true, true),
            description = "",
            keywords = setOf("foo", "baz"),
            level = "Preciso testar minha ideia!",
            name = "ABC0123 - Foo de baz bar",
            nature = "Graduação",
            offeringPeriod = "2022.2",
            start_date = "N/D",
            unity = "IME",
            url = "https://uspdigital.usp.br/jupiterweb"
        ),
        DisciplineModel(
            campus = "Butantã",
            category = DisciplineCategory(true, true, true, true),
            description = "",
            keywords = setOf("foo", "baz"),
            level = "Tópicos avançados em Empreendedorismo",
            name = "BCD1234 - Foo de baz bar",
            nature = "Pós-graduação",
            offeringPeriod = "2022.2",
            start_date = "N/D",
            unity = "IME",
            url = "https://uspdigital.usp.br/jupiterweb"
        ),
    )
}