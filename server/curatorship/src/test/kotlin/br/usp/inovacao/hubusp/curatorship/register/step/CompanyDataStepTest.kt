package br.usp.inovacao.hubusp.curatorship.register.step

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
    fun `it throws an error when company data has blank cnpj`() {
        assertFailsWith<StepValidationException> { validStep.copy(cnpj = "").validate() }
    }

    // TODO: Check each validation error
}
