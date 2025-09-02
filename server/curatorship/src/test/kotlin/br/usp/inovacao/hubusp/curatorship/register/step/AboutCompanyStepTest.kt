package br.usp.inovacao.hubusp.curatorship.register.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class AboutCompanyStepTest {
    @Test
    fun `it does not throws an error when AboutCompanyStep is valid`() {
        validStep.validate()
    }

    @Test
    fun `it throws an error when AboutCompanyStep description is empty`() {
        assertFailsWith<StepValidationException> { validStep.copy(description = "").validate() }
    }

    private val validStep =
        AboutCompanyStep(
            description = "Empresa para testes",
            logo = "https://www.logodofulanodetal.com",
            services = setOf("Website", "Blog"),
            technologies = setOf("Aplicativos", "Biomateriais"),
            site = "https://www.fulanodetal.com",
            odss = setOf("1 - Erradicação da Pobreza"),
            socialMedias = setOf("https://www.fulanodetal.com"),
        )
}
