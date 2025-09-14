package br.usp.inovacao.hubusp.curatorship.sheets

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class CompanyTest {
    @Test
    fun `it is valid with parse of a valid row`() {
        val row: List<String?> = CompanyTestHelp.validRow
        val result = Company.fromRow(row)

        result.validate()
    }

    @Test
    fun `it is valid with a link to logo`() {
        val urlLogo = "https://api.automatedtests/url/to/logo.png"
        val company = valid.copy(logo = urlLogo)

        company.validate()
    }

    @Test
    fun `it is invalid without required name`() {
        assertFailsWith<ValidationException> { valid.copy(name = "").validate() }
    }

    @Test
    fun `it is invalid without required cnpj`() {
        assertFailsWith<ValidationException> { valid.copy(cnpj = "").validate() }
    }

    @Test
    fun `it is invalid without required incubated`() {
        assertFailsWith<ValidationException> { valid.copy(incubated = "").validate() }
    }

    @Test
    fun `it is invalid without required year`() {
        assertFailsWith<ValidationException> { valid.copy(year = "").validate() }
    }

    // ecosystems, address, corporate_name
    @Test
    fun `it is invalid without required ecosystems`() {
        assertFailsWith<ValidationException> {
            valid.copy(incubated = "Sim").copy(ecosystems = emptySet()).validate()
        }
    }

    @Test
    fun `it doesn't check ecosystems if it wasn't incubated`() {
        valid.copy(incubated = "Não").copy(ecosystems = emptySet()).validate()
    }

    @Test
    fun `it is invalid without required address city`() {
        assertFailsWith<ValidationException> {
            valid.copy(address = validAddress.copy(city = "")).validate()
        }
    }

    @Test
    fun `it is invalid without required corporateName`() {
        assertFailsWith<ValidationException> { valid.copy(corporateName = "").validate() }
    }

    @Test
    fun `it is invalid with a malformed cnpj`() {
        val invalidCnpjs =
            listOf(
                "123.123",
                "123.456.789-10",
                "12345678000130",
                "11.123.321/000120",
                "11123.321/0001-20",
                "11.123.321/0001-2",
            )
        invalidCnpjs.forEach {
            assertFailsWith<ValidationException> { valid.copy(cnpj = it).validate() }
        }
    }

    @Test
    fun `it is valid with a foreign indicator cnpj`() {
        val company = valid.copy(cnpj = "Exterior9")

        company.validate()
    }

    @Test
    fun `it is valid with an empty phone set`() {
        val company = valid.copy(phones = emptySet())

        company.validate()
    }

    @Test
    fun `it is invalid with invalid years`() {
        val invalidYears = listOf("dois mil", "124", "2999")

        invalidYears.forEach {
            assertFailsWith<ValidationException> { valid.copy(year = it).validate() }
        }
    }

    @Test
    fun `it is invalid with invalid logo`() {
        assertFailsWith<ValidationException> { valid.copy(logo = "foo bar baz").validate() }
    }

    @Test
    fun `it is invalid with invalid url`() {
        assertFailsWith<ValidationException> { valid.copy(url = "foo bar baz").validate() }
    }

    @Test
    fun `it does not fail to parse when partners row is empty`() {
        val row: List<String?> = CompanyTestHelp.validRow
        val nullOffset = MutableList(30) { null }
        val rowWithNullPartners = row.subList(0, 33) + nullOffset + row.subList(62, row.size)

        val result = Company.fromRow(rowWithNullPartners)

        result.validate()
    }

    @Test
    fun `it does not fail when the partners list is empty`() {
        val company = valid.copy(partners = emptyList())

        company.validate()
    }

    @Test
    fun `it does not fail when the only partner has wrong unity`() {
        val partner = validPartner.copy(unity = "IME")
        val company = valid.copy(partners = listOf(partner))

        company.validate()
    }

    @Test
    fun `it does not fail when the only partner has wrong bond`() {
        val partner = validPartner.copy(bond = "James")
        val company = valid.copy(partners = listOf(partner))

        company.validate()
    }

    @Test
    fun `it does not convert logo link to drive when it is not a drive file id`() {
        val company = valid.copy(logo = "https://api.automatedtests/url/to/logo.png")

        assertEquals(company.logo, "https://api.automatedtests/url/to/logo.png")
    }

    @Test
    fun `it creates a correct company size when it is not an industrial business`() {
        val classification =
            validClassification.copy(
                major = "Comércio e Serviços",
                minor = "Informação e Comunicação",
            )

        val mappings =
            listOf(
                mapOf("employees" to "0", "expected" to setOf("Não Informado")),
                mapOf("employees" to "5", "expected" to setOf("Microempresa")),
                mapOf("employees" to "10", "expected" to setOf("Pequena Empresa")),
                mapOf("employees" to "50", "expected" to setOf("Média Empresa")),
                mapOf("employees" to "100", "expected" to setOf("Grande Empresa")),
            )

        mappings.forEach {
            val size = Company.calculateSize(it["employees"] as String?, "", classification)
            assertEquals(it["expected"], size)
        }
    }

    @Test
    fun `it creates a correct company size when it is an industrial business`() {
        val classification =
            validClassification.copy(
                major = "Indústria de Transformação",
                minor = "Produtos Diversos",
            )

        val mappings =
            listOf(
                mapOf("employees" to "0", "expected" to setOf("Não Informado")),
                mapOf("employees" to "5", "expected" to setOf("Microempresa")),
                mapOf("employees" to "20", "expected" to setOf("Pequena Empresa")),
                mapOf("employees" to "100", "expected" to setOf("Média Empresa")),
                mapOf("employees" to "500", "expected" to setOf("Grande Empresa")),
            )

        mappings.forEach {
            val size = Company.calculateSize(it["employees"] as String?, "", classification)
            assertEquals(it["expected"], size)
        }
    }

    @Test
    fun `it creates a correct unicorn company size`() {
        val classification = validClassification.copy()
        val size = Company.calculateSize("0", "Unicórnio", classification)

        assertContains(size, "Unicórnio")
    }

    @Test
    fun `it creates a correct comercial company classification with cnae`() {
        val cnae = "46.00-0-00"
        val expectedClassification =
            validClassification.copy(
                major = "Comércio e Serviços",
                minor = "Comércio por Atacado, exceto Veículos Automotores e Motocicletas",
            )

        val calculatedClassification = CompanyClassification.classify(cnae)

        assertEquals(calculatedClassification, expectedClassification)
    }

    @Test
    fun `it creates a correct agricultural company classification with cnae`() {
        val cnae = "01.00-0-00"
        val expectedClassification =
            validClassification.copy(
                major = "Agricultura, Pecuária, Pesca e Extrativismo",
                minor = "Agricultura, Pecuária, Produção Florestal, Pesca e Aquicultura",
            )

        val calculatedClassification = CompanyClassification.classify(cnae)

        assertEquals(calculatedClassification, expectedClassification)
    }

    @Test
    fun `it creates an empty classification when used an invalid cnae`() {
        val invalidCnaes = listOf("", "89.00-0-00", "371.00-0-00")
        val expectedClassification = validClassification.copy(major = "", minor = "")

        invalidCnaes.forEach {
            val calculatedClassification = CompanyClassification.classify(it)

            assertEquals(calculatedClassification, expectedClassification)
        }
    }

    private val valid = CompanyTestHelp.VALID_COMPANY_RECORD.copy()
    private val validAddress = CompanyTestHelp.VALID_ADDRESS_RECORD.copy()
    private val validPartner = CompanyTestHelp.VALID_PARTNER_RECORD.copy()
    private val validClassification = CompanyTestHelp.VALID_CLASSIFICATION_RECORD.copy()
}
