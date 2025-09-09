package br.usp.inovacao.hubusp.curatorship.register.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class AboutCompanyStepTest {
    companion object {
        val VALID_STEP =
            AboutCompanyStep(
                description = "Empresa para testes",
                logo = "https://www.logodofulanodetal.com",
                services = setOf("Website", "Blog"),
                technologies = setOf("Aplicativos", "Biomateriais"),
                site = "https://www.fulanodetal.com",
                odss = setOf("1 - Erradicação da Pobreza"),
                socialMedias = setOf("https://www.fulanodetal.com"),
            )
        val INVALID_STEP =
            AboutCompanyStep(
                description = "",
                logo = "hello world",
                services = setOf(),
                technologies = setOf(),
                site = "hello world",
                odss = setOf("some ivalid value"),
                socialMedias = setOf("hello world"),
            )
    }

    private val validStep = VALID_STEP
    private val invalidStep = INVALID_STEP

    @Test
    fun `it does not throws an error when AboutCompanyStep is valid`() {
        validStep.validate()
    }

    @Test
    fun `it does not throws an error when AboutCompanyStep is invalid`() {
        assertFailsWith<StepValidationException> { invalidStep.validate() }
    }

    @Test
    fun `it throws an error when AboutCompanyStep description is invalid`() {
        assertFailsWith<StepValidationException> { validStep.copy(description = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(description = null).validate() }
    }

    @Test
    fun `it throws an error when AboutCompanyStep logo is invalid`() {
        assertFailsWith<StepValidationException> { validStep.copy(logo = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(logo = null).validate() }
    }

    @Test
    fun `it throws an error when AboutCompanyStep site is invalid`() {
        assertFailsWith<StepValidationException> { validStep.copy(site = "").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(site = "foobar").validate() }
        assertFailsWith<StepValidationException> { validStep.copy(site = null).validate() }
    }

    @Test
    fun `it throws an error when AboutCompanyStep odss is invalid`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(odss = setOf("foobar")).validate()
        }
    }

    @Test
    fun `it throws an error when AboutCompanyStep socialMedias is invalid`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(socialMedias = setOf("foobar")).validate()
        }
    }
}
