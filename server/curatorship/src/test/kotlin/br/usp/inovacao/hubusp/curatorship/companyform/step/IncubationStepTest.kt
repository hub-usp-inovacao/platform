package br.usp.inovacao.hubusp.curatorship.companyform.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class IncubationStepTest {
    companion object {
        val VALID_STEP = IncubationStep(wasIncubated = "Sim. Something", ecosystem = "Something")
        val INVALID_STEP = IncubationStep(wasIncubated = "Sim", ecosystem = "")
    }

    private val validIncubated = VALID_STEP

    private val validNonIncubated = IncubationStep(wasIncubated = "Não", ecosystem = null)

    @Test
    fun `it does not throws an error when IncubationStep is valid`() {
        validIncubated.validate()
        validNonIncubated.validate()
    }

    @Test
    fun `it throws an error when company was incubated, but has blank ecosystem`() {

        assertFailsWith<StepValidationException> { validIncubated.copy(ecosystem = "").validate() }
    }

    @Test
    fun `it throws an error when company was incubated, but has null ecosystem`() {
        assertFailsWith<StepValidationException> {
            validIncubated.copy(ecosystem = null).validate()
        }
    }
}
