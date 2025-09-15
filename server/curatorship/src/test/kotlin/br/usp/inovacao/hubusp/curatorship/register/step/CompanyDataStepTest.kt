package br.usp.inovacao.hubusp.curatorship.register.step

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFailsWith

class CompanyDataStepTest {
    companion object {
        val VALID_STEP =
            CompanyDataStep(
                cnpj = "00.000.000/0001-00",
                publicName = "Some public name",
                corporateName = "Some corporate name",
                year = "2025",
                size = "MEI",
                cnae = "00.00-0-00",
                registryStatus = "Ativa",
                phones = setOf("(11) 99999-4433"),
                emails = setOf("test@example.com"),
                street = "Rua tal",
                neighborhood = "Vila tal",
                city = "Cidade tal",
                state = "Estado tal",
                zipcode = "00000-000",
                companyNature = "000-0 - Automated Tests",
            )
        val INVALID_STEP =
            CompanyDataStep(
                cnpj = "",
                publicName = "",
                corporateName = "",
                year = "9000",
                size = "",
                cnae = "hello world",
                registryStatus = "hello world",
                phones = setOf("hello world"),
                emails = setOf("hello world"),
                street = "",
                neighborhood = "",
                city = "",
                state = "",
                zipcode = "hello world",
                companyNature = "hello world",
            )
    }

    private val validStep = VALID_STEP

    @Test
    fun `it does not throws an error when CompanyDataStep is valid`() {
        validStep.validate()
    }

    @Test
    fun `it throws an error when company data has invalid cnpj`() {
        assertFailsWith<StepValidationException> { validStep.copy(cnpj = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(cnpj = null).validate() }
        assertFailsWith<StepValidationException> { validStep.copy(cnpj = "foobar").validate() }
    }

    @Test
    fun `it throws an error when company data has invalid publicName`() {
        assertFailsWith<StepValidationException> { validStep.copy(publicName = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(publicName = null).validate() }
    }

    @Test
    fun `it throws an error when company data has invalid corporateName`() {
        assertFailsWith<StepValidationException> { validStep.copy(corporateName = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(corporateName = null).validate() }
    }

    @Test
    fun `it throws an error when company data has invalid year`() {
        assertFailsWith<StepValidationException> { validStep.copy(year = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(year = null).validate() }
        assertFailsWith<StepValidationException> { validStep.copy(year = "0").validate() }
        assertFailsWith<StepValidationException> {
            validStep.copy(year = (LocalDate.now().year + 1).toString()).validate()
        }
    }

    @Test
    fun `it throws an error when company data has invalid size`() {
        assertFailsWith<StepValidationException> { validStep.copy(size = "foobar").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(size = null).validate() }
    }

    @Test
    fun `it throws an error when company data has invalid cnae`() {
        assertFailsWith<StepValidationException> { validStep.copy(cnae = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(cnae = null).validate() }
        assertFailsWith<StepValidationException> { validStep.copy(cnae = "12345678").validate() }
    }

    @Test
    fun `it throws an error when company data has invalid registryStatus`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(registryStatus = "foobar").validate()
        }
        assertFailsWith<StepValidationException> {
            validStep.copy(registryStatus = null).validate()
        }
    }

    @Test
    fun `it throws an error when company data has invalid phones`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(phones = setOf("foobar")).validate()
        }
    }

    @Test
    fun `it throws an error when company data has invalid emails`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(emails = setOf("foobar")).validate()
        }
    }

    @Test
    fun `it throws an error when company data has invalid street`() {
        assertFailsWith<StepValidationException> { validStep.copy(street = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(street = null).validate() }
    }

    @Test
    fun `it throws an error when company data has invalid zipcode`() {
        assertFailsWith<StepValidationException> { validStep.copy(zipcode = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(zipcode = null).validate() }
        assertFailsWith<StepValidationException> {
            validStep.copy(zipcode = "000000000").validate()
        }
    }

    @Test
    fun `it throws an error when company data has invalid companyNature`() {
        assertFailsWith<StepValidationException> { validStep.copy(companyNature = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(companyNature = null).validate() }
        assertFailsWith<StepValidationException> {
            validStep.copy(companyNature = "foobar").validate()
        }
    }
}
