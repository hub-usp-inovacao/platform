package br.usp.inovacao.hubusp.curatorship.companyform.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class InvestmentStepTest {
    companion object {
        val VALID_STEP =
            InvestmentStep(
                received = "Sim",
                investmentsReceived = setOf("Investmento próprio"),
                own = "R$ 1,00",
                angel = "R$ 0,00",
                venture = "R$ 0,00",
                equity = "R$ 0,00",
                pipe = "R$ 0,00",
                others = "R$ 0,00",
            )
        val INVALID_STEP =
            InvestmentStep(
                received = "Sim",
                investmentsReceived = emptySet(),
                own = "",
                angel = "",
                venture = "",
                equity = "",
                pipe = "",
                others = "",
            )
    }

    private val validStep = VALID_STEP.copy()

    @Test
    fun `it does not throws an error when InvestmentStep is valid`() {
        validStep.validate()
    }

    @Test
    fun `it throws an error when own investment is null`() {
        assertFailsWith<StepValidationException> { validStep.copy(own = null).validate() }
    }

    @Test
    fun `it throws an error when angel investment is null`() {
        assertFailsWith<StepValidationException> { validStep.copy(angel = null).validate() }
    }

    @Test
    fun `it throws an error when venture investment is null`() {
        assertFailsWith<StepValidationException> { validStep.copy(venture = null).validate() }
    }

    @Test
    fun `it throws an error when equity investment is null`() {
        assertFailsWith<StepValidationException> { validStep.copy(equity = null).validate() }
    }

    @Test
    fun `it throws an error when pipe investment is null`() {
        assertFailsWith<StepValidationException> { validStep.copy(pipe = null).validate() }
    }

    @Test
    fun `it throws an error when others investment is null`() {
        assertFailsWith<StepValidationException> { validStep.copy(others = null).validate() }
    }

    @Test
    fun `it throws an error when own investment is not a number`() {
        assertFailsWith<StepValidationException> { validStep.copy(own = "hello world").validate() }
    }

    @Test
    fun `it throws an error when angel investment is not a number`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(angel = "hello world").validate()
        }
    }

    @Test
    fun `it throws an error when venture investment is not a number`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(venture = "hello world").validate()
        }
    }

    @Test
    fun `it throws an error when equity investment is not a number`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(equity = "hello world").validate()
        }
    }

    @Test
    fun `it throws an error when pipe investment is not a number`() {
        assertFailsWith<StepValidationException> { validStep.copy(pipe = "hello world").validate() }
    }

    @Test
    fun `it throws an error when received is set to true, but investments received is empty`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(investmentsReceived = emptySet()).validate()
        }
    }

    @Test
    fun `it does not throw an error when received is set to false and investments received is empty`() {
        validStep.copy(received = "Não", investmentsReceived = emptySet()).validate()
    }
}
