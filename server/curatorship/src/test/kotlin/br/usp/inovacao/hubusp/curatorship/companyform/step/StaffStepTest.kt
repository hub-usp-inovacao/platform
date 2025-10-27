package br.usp.inovacao.hubusp.curatorship.companyform.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class StaffStepTest {
    companion object {
        val VALID_STEP =
            StaffStep(
                numberOfCltEmployees = "00", numberOfPjColaborators = "00", numberOfInterns = "00")
        val INVALID_STEP =
            StaffStep(
                numberOfCltEmployees = "hello",
                numberOfPjColaborators = "foo",
                numberOfInterns = "bar")
    }

    private val validStep = VALID_STEP

    @Test
    fun `it does not throws an error when StaffStep is valid`() {
        validStep.validate()
    }

    @Test
    fun `it throws an error when number of CLT employees is null`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(numberOfCltEmployees = null).validate()
        }
    }

    @Test
    fun `it throws an error when number of CLT employees is not a number`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(numberOfCltEmployees = "hello1world").validate()
        }
    }

    @Test
    fun `it throws an error when number of PJ colaborators is null`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(numberOfPjColaborators = null).validate()
        }
    }

    @Test
    fun `it throws an error when number of PJ colaborators is not a number`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(numberOfPjColaborators = "hello1world").validate()
        }
    }

    @Test
    fun `it throws an error when number of interns is null`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(numberOfInterns = null).validate()
        }
    }

    @Test
    fun `it throws an error when number of interns is not a number`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(numberOfInterns = "hello1world").validate()
        }
    }
}
