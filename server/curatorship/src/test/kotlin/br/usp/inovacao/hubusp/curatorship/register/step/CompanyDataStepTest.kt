package br.usp.inovacao.hubusp.curatorship.register.step

import br.usp.inovacao.hubusp.curatorship.register.CompanyFormTestHelp
import kotlin.test.Test

class CompanyDataStepTest {
    @Test
    fun `it does not throws an error when CompanyDataStep is valid`() {
        validStep.validate()
    }

    // TODO: Check each validation error

    private val validStep = CompanyFormTestHelp.VALID_COMPANY_DATA.copy()
}
