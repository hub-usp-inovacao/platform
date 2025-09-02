package br.usp.inovacao.hubusp.curatorship.register.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class RevenueStepTest {
    @Test
    fun `it does not throws an error when RevenueStep is valid`() {
        validStep.validate()
    }

    @Test
    fun `it throws an error when last year's revenue is null`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(lastYearRevenue = null).validate()
        }
    }

    @Test
    fun `it throws an error when last year's revenue is not a number`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(lastYearRevenue = "hello world").validate()
        }
    }

    @Test
    fun `it does not throws an error when last year's revenue doesn't start with 'R$ '`() {
        validStep.copy(lastYearRevenue = "1234.567,89").validate()
    }

    private val validStep = RevenueStep(lastYearRevenue = "R$ 1.234.567,89")
}
