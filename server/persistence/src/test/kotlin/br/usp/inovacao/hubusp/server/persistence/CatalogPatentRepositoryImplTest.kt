package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.*
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.deleteMany
import org.litote.kmongo.getCollection
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

internal class CatalogPatentRepositoryImplTest {
    private lateinit var testDb: MongoDatabase
    private lateinit var underTest: CatalogPatentRepositoryImpl

    @BeforeTest
    fun setup() {
        testDb = connectToTestDb()
        underTest = CatalogPatentRepositoryImpl(testDb)
        seedTestDb()
    }

    @AfterTest
    fun teardown() {
        cleanTestDb()
    }

    @Test
    fun `it filters by major`() {
        // given
        val major = "A - Necessidades Humanas"
        val params = PatentSearchParams(majorAreas = setOf(major))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all {
            it.classification.primary.cip == major || it.classification.secondary?.cip == major
        } }
    }

    @Test
    fun `it filters by minor`() {
        // given
        val minor = "A61 - Ciência médica ou veterinária; higiene"
        val params = PatentSearchParams(minorAreas = setOf(minor))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all {
            it.classification.primary.subarea == minor || it.classification.secondary?.subarea == minor
        } }
    }

    @Test
    fun `it filters by status`() {
        // given
        val status = "Concedida"
        val params = PatentSearchParams(status = status)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.status == status } }
    }

    @Test
    fun `it filters by single-token text`() {
        // given
        val term = "cepa"
        val params = PatentSearchParams(term = term)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all{
            it.name.contains(term) || it.summary.contains(term) || it.owners.any { owner -> owner.contains(term) } || it.inventors.any { inventor -> inventor.contains(term) }
        } }
    }

    @Test
    fun `it filters by all fields`() {
        // given
        val major = "A - Necessidades Humanas"
        val minor = "A61 - Ciência médica ou veterinária; higiene"
        val status = "Concedida"
        val term = "cepa"
        val params = PatentSearchParams(
            majorAreas = setOf(major),
            minorAreas = setOf(minor),
            status = status,
            term = term
        )

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all {
            (it.classification.primary.cip == major || it.classification.secondary?.cip == major) &&
                    (it.classification.primary.subarea == minor || it.classification.secondary?.subarea == minor) &&
                    it.status == status &&
                    (it.name.contains(term) || it.summary.contains(term) || it.owners.any { owner -> owner.contains(term) } || it.inventors.any { inventor -> inventor.contains(term) })
        } }
    }

    @Test
    fun `it reduces to all classifications`() {
        // given
        cleanTestDb()
        seedDbForClassifications()

        // when
        val result = underTest.getClassifications()

        // then
        val expected = setOf(
            IPC(primary = "A", secondaries = setOf("A61", "A62")),
            IPC(primary = "B", secondaries = setOf("B01"))
        )

        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { expected.contains(it) } }
    }

    private fun seedDbForClassifications() {
        val patentCollection = testDb.getCollection<Patent>("patents")
        patentCollection.insertMany(testSeedsForClassifications())
    }

    private fun testSeedsForClassifications() = listOf(
        Patent(
            name = "Cepa transgênica de Saccharomyces sp. e método de obtenção da cepa tra…",
            summary = "Cepa transgênica de Saccharomyces sp. e método de obtenção da cepa tra…",
            classification = DualClassification(
                primary = Area(
                    cip = "A",
                    subarea = "A61"
                ),
                secondary = Area(
                    cip = "A",
                    subarea = "A62"
                )
            ),
            ipcs = setOf("A61K003847","A61P003104","C12N000119","C12N000936","C12N001556","C12N001581"),
            owners = setOf("Univ. São Paulo"),
            status = "Concedida",
            url = "https://www.derwentinnovation.com/tip-innovation/externalLink.do?data=…",
            inventors = setOf("Elza Teresinha Grael"," Ana Clara Guerrini Schenberg"," Elisabete Jose Vicente"),
            countriesWithProtection = setOf("Brasil"),
        ),
        Patent(
            name = "Cepa transgênica de Saccharomyces sp. e método de obtenção da cepa tra… 2",
            summary = "Cepa transgênica de Saccharomyces sp. e método de obtenção da cepa tra…",
            classification = DualClassification(
                primary = Area(
                    cip = "B",
                    subarea = "B01"
                )
            ),
            ipcs = setOf("A61K003847","A61P003104","C12N000119","C12N000936","C12N001556","C12N001581"),
            owners = setOf("Univ. São Paulo"),
            status = "Concedida",
            url = "https://www.derwentinnovation.com/tip-innovation/externalLink.do?data=…",
            inventors = setOf("Elza Teresinha Grael"," Ana Clara Guerrini Schenberg"," Elisabete Jose Vicente"),
            countriesWithProtection = setOf("Brasil"),
        ),
    )

    private fun cleanTestDb() {
        val patentCollection = testDb.getCollection<Patent>("patents")
        patentCollection.deleteMany("{}")
    }

    private fun seedTestDb() {
        val patentCollection = testDb.getCollection<Patent>("patents")
        patentCollection.insertMany(testSeeds())
    }

    private fun testSeeds() = listOf(
        Patent(
            name = "Cepa transgênica de Saccharomyces sp. e método de obtenção da cepa tra…",
            summary = "Cepa transgênica de Saccharomyces sp. e método de obtenção da cepa tra…",
            classification = DualClassification(
                primary = Area(
                    cip = "A - Necessidades Humanas",
                    subarea = "A61 - Ciência médica ou veterinária; higiene"
                ),
                secondary = Area(
                    cip = "A - Necessidades Humanas",
                    subarea = "A61 - Ciência médica ou veterinária; higiene"
                )
            ),
            ipcs = setOf("A61K003847","A61P003104","C12N000119","C12N000936","C12N001556","C12N001581"),
            owners = setOf("Univ. São Paulo"),
            status = "Concedida",
            url = "https://www.derwentinnovation.com/tip-innovation/externalLink.do?data=…",
            inventors = setOf("Elza Teresinha Grael"," Ana Clara Guerrini Schenberg"," Elisabete Jose Vicente"),
            countriesWithProtection = setOf("Brasil"),
        ),
        Patent(
            name = "Cepa transgênica de Saccharomyces sp. e método de obtenção da cepa tra… 2",
            summary = "Cepa transgênica de Saccharomyces sp. e método de obtenção da cepa tra…",
            classification = DualClassification(
                primary = Area(
                    cip = "C - Química;Metalurgia",
                    subarea = "C07 - Química orgânica"
                ),
            ),
            ipcs = setOf("A61K003847","A61P003104","C12N000119","C12N000936","C12N001556","C12N001581"),
            owners = setOf("Univ. São Paulo"),
            status = "Em análise",
            url = "https://www.derwentinnovation.com/tip-innovation/externalLink.do?data=…",
            inventors = setOf("Elza Teresinha Grael","Ana Clara Guerrini Schenberg","Elisabete Jose Vicente"),
            countriesWithProtection = setOf("Brasil"),
        )
    )
}