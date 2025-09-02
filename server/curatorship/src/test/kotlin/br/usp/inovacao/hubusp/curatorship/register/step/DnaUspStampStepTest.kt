package br.usp.inovacao.hubusp.curatorship.register.step

import kotlin.test.Test
import kotlin.test.assertFailsWith

class DnaUspStampStepTest {
    companion object {
        val VALID_STEP =
            DnaUspStampStep(
                wantsStamp = true,
                truthfulInformations = true,
                permissions =
                    setOf(
                        "Permito o envio de e-mails para ser avisado sobre eventos e oportunidades relevantes à empresa",
                        "Permito a divulgação das informações públicas na plataforma Hub USPInovação",
                        "Permito a divulgação das informações públicas na plataforma Hub USPInovação e também para unidades da USP",
                    ),
                email = "fulano@mail.com",
                name = "fulano de tal",
            )
    }

    private val validStep = VALID_STEP

    private val validStepWithoutStamp =
        DnaUspStampStep(
            wantsStamp = false,
            truthfulInformations = true,
            permissions =
                setOf(
                    "Permito o envio de e-mails para ser avisado sobre eventos e oportunidades relevantes à empresa",
                    "Permito a divulgação das informações públicas na plataforma Hub USPInovação",
                    "Permito a divulgação das informações públicas na plataforma Hub USPInovação e também para unidades da USP",
                ),
            email = "",
            name = "",
        )

    @Test
    fun `it does not throws an error when DnaUspStampStep is valid`() {
        validStep.validate()
    }

    @Test
    fun `it does not throw an error when doesn't want stamp`() {
        validStep.copy(wantsStamp = false).validate()
    }

    @Test
    fun `it throws an error when truthfulInformations is false`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(truthfulInformations = false).validate()
        }
    }

    @Test
    fun `it throws an error when wants stamp but email or name is null`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(name = null).validate()
            validStep.copy(email = null).validate()
        }
    }

    @Test
    fun `it throws an error when wants stamp but email or name is blank`() {
        assertFailsWith<StepValidationException> {
            validStep.copy(name = "").validate()
            validStep.copy(email = "").validate()
        }
    }

    @Test
    fun `it doesn't throw an error when doesn't want stamp and email or name is null`() {
        validStepWithoutStamp.copy(name = null).validate()
        validStepWithoutStamp.copy(email = null).validate()
    }
}
