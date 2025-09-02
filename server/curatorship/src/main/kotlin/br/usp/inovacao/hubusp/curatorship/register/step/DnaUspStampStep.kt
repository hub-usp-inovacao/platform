package br.usp.inovacao.hubusp.curatorship.register.step

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.isTrue
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class DnaUspStampStep(
    @SerialName("wants") val wantsStamp: Boolean,
    val name: String?,
    val email: String?,
    @SerialName("truthful_informations") val truthfulInformations: Boolean,

    /**
     * One of:
     *
     * "Permito o envio de e-mails para ser avisado sobre eventos e oportunidades relevantes à
     * empresa",
     *
     * "Permito a divulgação das informações públicas na plataforma Hub USPInovação",
     *
     * "Permito a divulgação das informações públicas na plataforma Hub USPInovação e também para
     * unidades da USP",
     *
     * TODO: Refactor this (separate boolean members for each option? enum? etc)
     */
    val permissions: Set<String>?,
) {
    companion object {}

    @Throws(StepValidationException::class)
    fun validate() =
        try {
            validate(this) {
                if (wantsStamp) {
                    validate(DnaUspStampStep::name).isNotNull().isNotBlank()
                    validate(DnaUspStampStep::email).isNotNull().isNotBlank().isEmail()
                }
                validate(DnaUspStampStep::truthfulInformations).isTrue()
            }
        } catch (cve: ConstraintViolationException) {
            throw StepValidationException.from(cve)
        }
}
