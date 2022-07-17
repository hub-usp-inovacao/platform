package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.Contact
import br.usp.inovacao.hubusp.server.catalog.Initiative
import br.usp.inovacao.hubusp.server.catalog.InitiativeSearchParams
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.deleteMany
import org.litote.kmongo.getCollection
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

internal class CatalogInitiativeRepositoryImplTest {
    private lateinit var testDb: MongoDatabase
    private lateinit var underTest: CatalogInitiativeRepositoryImpl

    @BeforeTest
    fun setup() {
        testDb = connectToTestDb()
        underTest = CatalogInitiativeRepositoryImpl(testDb)
        seedTestDb()
    }

    @AfterTest
    fun teardown() {
        cleanTestDb()
    }

    @Test
    fun `it filters by a single classification`() {
        // given
        val classification = "Agente Institucional"
        val params = InitiativeSearchParams(classifications = setOf(classification))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.classification == classification } }
    }

    @Test
    fun `it filters by multiple classifications`() {
        // given
        val classification1 = "Agente Institucional"
        val classification2 = "Entidade Estudantil"
        val params = InitiativeSearchParams(classifications = setOf(classification1, classification2))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.classification == classification1 || it.classification == classification2 } }
    }

    @Test
    fun `it filters by campus`() {
        // given
        val campus = "Butantã"
        val params = InitiativeSearchParams(campus = campus)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.localization == campus } }
    }

    private fun seedTestDb() {
        val initiativeCollection = testDb.getCollection<Initiative>("iniciatives")
        initiativeCollection.insertMany(testSeeds())
    }

    private fun testSeeds() = listOf(
        Initiative(
            classification ="Agente Institucional",
            name = "Agência USP de Inovação (AUSPIN)",
            localization = "Toda a USP",
            unity = "N/D",
            tags = setOf("Patentes"," Marcas"," Software"," Empreendedorismo"," Licenciamento"),
            url = "http://www.inovacao.usp.br/",
            description = "A Agência USP de Inovação é o Núcleo de Inovação Tecnológica da USP, r…",
            email = "auspin@usp.br",
            contact = Contact(
                person = "",
                info = "(11) 3091-4165"
            )
        ),
        Initiative(
            classification ="Entidade Estudantil",
            name = "Agência USP de Inovação (AUSPIN)",
            localization = "Butantã",
            unity = "N/D",
            tags = setOf("Patentes"," Marcas"," Software"," Empreendedorismo"," Licenciamento"),
            url = "http://www.inovacao.usp.br/",
            description = "A Agência USP de Inovação é o Núcleo de Inovação Tecnológica da USP, r…",
            email = "auspin@usp.br",
            contact = Contact(
                person = "",
                info = "(11) 3091-4165"
            )
        )
    )

    private fun cleanTestDb() {
        val initiativeCollection = testDb.getCollection<Initiative>("iniciatives")
        initiativeCollection.deleteMany("{}")
    }
}