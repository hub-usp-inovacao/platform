package br.usp.inovacao.hubusp.curatorship.register.step

import br.usp.inovacao.hubusp.curatorship.register.CompanyFormTestHelp
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

    private val validStep = CompanyFormTestHelp.VALID_ABOUT.copy()
}
