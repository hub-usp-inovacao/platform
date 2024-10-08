package br.usp.inovacao.hubusp.server.persistence

import br.usp.inovacao.hubusp.server.catalog.PDISearchParams
import br.usp.inovacao.hubusp.server.persistence.models.PDIModel
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.deleteMany
import org.litote.kmongo.getCollection
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class CatalogPDIRepositoryImplTest {

    private lateinit var testDb: MongoDatabase
    private lateinit var underTest: CatalogPDIRepositoryImpl

    @BeforeTest
    fun setup() {
        testDb = connectToTestDb()
        underTest = CatalogPDIRepositoryImpl(testDb)
        seedTestDb()
    }

    @AfterTest
    fun teardown() {
        cleanTestDb()
    }

    @Test
    fun `it filters by category`() {
        // given
        val category = "Centro de Pesquisa em Engenharia"
        val params = PDISearchParams(categories = setOf(category))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.category == category } }
    }

    @Test
    fun `it filters by multiple categories`() {
        // given
        val category1 = "Centro de Pesquisa em Engenharia"
        val category2 = "CEPID"
        val params = PDISearchParams(categories = setOf(category1, category2))

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.category == category1 || it.category == category2 } }
    }

    @Test
    fun `it filters by campus`() {
        // given
        val campus = "Ribeirão Preto"
        val params = PDISearchParams(campus = campus)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all { it.campus == campus } }
    }

    @Test
    fun `it filters by single-token text`() {
        // given
        val term = "multidispositivo"
        val params = PDISearchParams(term = term)

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all {
            it.name.contains(term) || it.coordinator.contains(term) || it.description.contains(term) || it.tags.any { tag -> tag.contains(term) }
        } }
    }

    @Test
    fun `it filters by all fields`() {
        // given
        val category = "Centro de Pesquisa em Engenharia"
        val campus = "Butantã"
        val term = "multidispositivo"
        val params = PDISearchParams(
            categories = setOf(category),
            campus = campus,
            term = term,
        )

        // when
        val result = underTest.filter(params)

        // then
        assertTrue { result.isNotEmpty() }
        assertTrue { result.all {
            it.category == category && it.campus == campus && (
                    it.name.contains(term) || it.coordinator.contains(term) || it.description.contains(term) || it.tags.any { tag -> tag.contains(term) }
                    )
        } }
    }

    fun cleanTestDb() {
        val pdiCollection = testDb.getCollection<PDIModel>("pdis")

        pdiCollection.deleteMany("{}")
    }

    fun seedTestDb() {
        val pdiCollection = testDb.getCollection<PDIModel>("pdis")

        pdiCollection.insertMany(testSeeds())
    }

    fun testSeeds() = listOf(
        PDIModel (
            category = "CEPID",
            name = "CTC - Centro de Terapia Celular",
            campus = "Ribeirão Preto",
            unity = "Faculdade de Medicina",
            coordinator = "Dimas Tadeu Covas",
            site = "http://lgmb.fmrp.usp.br/bit/",
            email = "dimas@fmrp.usp.br",
            phone = "(16) 21019300",
            description = "Seu objetivo é desenvolver pesquisas para entender a biologia das células-tronco, bem como em desenvolver novas tecnologias para seu uso no tratamento de doenças. O grupo de pesquisadores envolve médicos, biólogos, profissionais biomédicos, farmacêuticos e veterinários, entre outros.",
            tags = setOf("Célula-tronco", "Ensaios clínicos")
        ),
        PDIModel (
            category = "Centro de Pesquisa em Engenharia",
            name = "C4AI - Centro de Inteligência Artificial",
            campus = "Butantã",
            unity = "",
            coordinator = "Fábio Cozman",
            site = "http://c4ai.inova.usp.br/",
            email = "c4ai@usp.br",
            phone = "n/d",
            description = "multidispositivo O Centro de Inteligência Artificial (Center for Artificial Intelligence - C4AI) tem o compromisso de desenvolver pesquisas no estado da arte em Inteligência Artificial (IA), explorando tanto aspectos básicos quanto aplicados nesta área. Com suporte da IBM e da Fundação de Amparo à Pesquisa do Estado de São Paulo (FAPESP), o C4AI também desenvolve estudos sobre o impacto social e econômico da IA e conduz atividades de disseminação de conhecimento e transferência de tecnologia, procurando formas de melhorar a qualidade de vida humana e incrementar diversidade e inclusão. O C4AI considera que o próximo nível de desempenho em IA só poderá ser alcançado enfatizando a combinação de aprendizado de máquina, tomada de decisão, representação de conhecimento e raciocínio, e incrementando a colaboração entre essas áreas e suas aplicações. A conexão entre as tópicos básicos de pesquisa e áreas de aplicação de IA funciona nos dois sentidos: os tópicos básicos permitem a abordagem de problemas de grande escala nas áreas de aplicação selecionadas, e por outro lado são alimentados pelos desafios de escala nessas áreas de aplicação.",
            tags = setOf("IA", "Dados", "Códigos abertos")
        )
    )
}