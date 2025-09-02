package br.usp.inovacao.hubusp.curatorship.register.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class PartnerStepTest {
    companion object {
        val VALID_PARTNER =
            Partner(
                name = "Some partner",
                nusp = "1234567",
                bond = "Some bond",
                unit = "Some unit",
                email = "test@example.com",
                phone = "(11) 99999-9999",
                role = "Some role",
            )

        val VALID_STEP = listOf(VALID_PARTNER)

        val INVALID_STEP =
            listOf(
                Partner(
                    name = "",
                    nusp = null,
                    bond = "",
                    unit = "",
                    email = "",
                    phone = "",
                    role = "",
                ),
            )
    }

    private val validStep = VALID_STEP
    private val validPartner = VALID_PARTNER

    @Test
    fun `it does not throws an error when PartnerStep is valid`() {
        validStep.validate()
    }

    @Test
    fun `it does not throws an error when Partner is valid`() {
        validPartner.validate()
    }

    @Test
    fun `it throws an error when partner has blank name`() {

        assertFailsWith<StepValidationException> { validPartner.copy(name = "").validate() }
    }

    @Test
    fun `it does not throw an error when partner has blank nusp`() {

        validPartner.copy(nusp = "").validate()
    }

    @Test
    fun `it throws an error when partner has blank bond`() {

        assertFailsWith<StepValidationException> { validPartner.copy(bond = "").validate() }
    }

    @Test
    fun `it throws an error when partner has blank unit`() {

        assertFailsWith<StepValidationException> { validPartner.copy(unit = "").validate() }
    }

    @Test
    fun `it throws an error when partner has invalid email`() {

        assertFailsWith<StepValidationException> {
            validPartner.copy(email = "hello world").validate()
        }
    }

    @Test
    fun `it throws an error when partner has phone without numbers`() {

        assertFailsWith<StepValidationException> {
            validPartner.copy(phone = "hello world").validate()
        }
    }
}
