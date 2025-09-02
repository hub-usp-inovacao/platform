package br.usp.inovacao.hubusp.curatorship.register.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class IncubationStepTest {
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

    private val validIncubated =
        IncubationStep(wasIncubated = "Sim. Something", ecosystem = "Something")

    private val validNonIncubated = IncubationStep(wasIncubated = "Não", ecosystem = null)
}
