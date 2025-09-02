package br.usp.inovacao.hubusp.curatorship.register

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertEquals
import br.usp.inovacao.hubusp.curatorship.register.step.StepValidationException

// TODO: Separate tests for each step
class CompanyStepTest {
    @Test
    fun `it does not throws an error when AboutCompanyStep is valid`() {
        validAbout.validate()
    }

    @Test
    fun `it throws an error when AboutCompanyStep description is empty`() {
        assertFailsWith<StepValidationException> {
            validAbout.copy(description = "").validate()
        }
    }

    private val validAbout = CompanyStepTestHelp.VALID_ABOUT.copy()
}
