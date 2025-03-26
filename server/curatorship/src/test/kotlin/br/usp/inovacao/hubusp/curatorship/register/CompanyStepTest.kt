package br.usp.inovacao.hubusp.curatorship.register

import kotlin.test.Test
import kotlin.test.assertFailsWith

class CompanyStepTest {

    @Test
    fun `it throws an error when about company description is empty`() {
        assertFailsWith<ValidationException> {
            validAbout.copy(description = "")
        }
    }

    private val validAbout = CompanyStepTestHelp.VALID_ABOUT.copy()
}