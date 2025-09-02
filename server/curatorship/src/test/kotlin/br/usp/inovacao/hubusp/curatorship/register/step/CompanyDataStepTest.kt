package br.usp.inovacao.hubusp.curatorship.register.step

import kotlin.test.Test

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
    }

    private val validStep = VALID_STEP

    @Test
    fun `it does not throws an error when CompanyDataStep is valid`() {
        validStep.validate()
    }

    // TODO: Check each validation error
}
