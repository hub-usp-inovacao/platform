package br.usp.inovacao.hubusp.server.persistence.curatorship

import br.usp.inovacao.hubusp.curatorship.sheets.Company
import br.usp.inovacao.hubusp.curatorship.sheets.CompanyAddress
import br.usp.inovacao.hubusp.curatorship.sheets.CompanyClassification
import br.usp.inovacao.hubusp.curatorship.sheets.Partner
import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection
import org.bson.Document
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class CompanyRepositoryImplTest() {

    private lateinit var testDb: MongoDatabase
    private lateinit var underTest: CompanyRepositoryImpl

    @BeforeTest
    fun setup() {
        testDb = connectToTestDb()
        underTest = CompanyRepositoryImpl(testDb)
        seedTestDb()
    }

    @AfterTest
    fun teardown() {
        cleanTestDb()
    }

    @Test
    fun `should save all companies`() {
        cleanTestDb()
        val companiesSaved = mutableSetOf<Company>()

        testSeeds().forEach {
            underTest.save(it)
            companiesSaved.add(it)
        }

        assertTrue(companiesSaved.isNotEmpty())
    }

    private fun cleanTestDb() {
        val companyCollection = testDb.getCollection<Company>("companies")
        val bson = Document()
        companyCollection.deleteMany(bson)
    }

    private fun seedTestDb() {
        val companyCollection = testDb.getCollection<Company>("companies")
        companyCollection.insertMany(testSeeds())
    }

    private fun testSeeds() = listOf(
        Company(
            active = true,
            address = CompanyAddress(
                cep = "05555-020",
                city = "São Paulo",
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            allowed = true,
            classification = CompanyClassification(
                major = "Comércio e Serviços",
                minor = "Serviços Domésticos"
            ),
            cnae = "042.0230-02",
            cnpj = "12.345.678/9012-34",
            companySize = setOf("Microempresa"),
            corporateName = "Empresa de Vendas",
            description = "pluralidade foo bar baz",
            ecosystems = setOf("InovaLab@POLI"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = setOf("Vendas", "Comércio"),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            logo = "https://foo-comp.com.br/logo.png",
            partners = listOf(
                Partner("Fulano", "1234567", "Aluno ou ex-aluno (graduação)",
                    "FEA", "fulano@example.com", "(11) 98899-7654"),
                Partner("Ciclano", "98765432", "Aluno ou ex-aluno (graduação)" ,
                    "IME", "cliclano@example.com", "(11) 98989-7676")
            ),
            year = "2019"
        ),
        Company(
            active = true,
            address = CompanyAddress(
                cep = "05555-020",
                city = "Rio de Janeiro",
                neighborhood = "Centro",
                state = "SP",
                venue = "Rua Barão de Itapetininga, 4"
            ),
            allowed = true,
            classification = CompanyClassification(
                major = "Infraestrutura e Construção",
                minor = "Construção"
            ),
            cnae = "042.0230-02",
            cnpj = "11.111.111/1110-00",
            companySize = setOf("Grande Empresa"),
            corporateName = "Empresa de Testes Automatizados",
            description = "foo bar baz",
            ecosystems = setOf("Habits"),
            emails = setOf("foo@example.com"),
            incubated = "Sim. A empresa já está graduada",
            name = "Foo inc.",
            phones = setOf("(11) 98899-7654"),
            services = setOf("Testes automatizados"),
            technologies = emptySet(),
            url = "https://foo-comp.com.br",
            logo = "https://drive.google.com/file",
            partners = listOf(
                Partner("Foobaz", "12345678", "Aluno Especial Pós Graduação",
                    "FEA", "foobaz@example.com", "(11) 99999-8888"),
            ),
            year = "2023"
        )
    )
}
