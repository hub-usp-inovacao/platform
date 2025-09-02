package br.usp.inovacao.hubusp.curatorship.register.step

import br.usp.inovacao.hubusp.curatorship.register.isNumber
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class StaffStep(
    @SerialName("number_of_CLT_employees") val numberOfCltEmployees: String?,
    @SerialName("number_of_PJ_colaborators") val numberOfPjColaborators: String?,
    @SerialName("number_of_interns") val numberOfInterns: String?
) {
    companion object {}

    @Throws(StepValidationException::class)
    fun validate() =
        try {
            validate(this) {
                validate(StaffStep::numberOfCltEmployees).isNotNull().isNumber()
                validate(StaffStep::numberOfPjColaborators).isNotNull().isNumber()
                validate(StaffStep::numberOfInterns).isNotNull().isNumber()
            }
        } catch (cve: ConstraintViolationException) {
            throw StepValidationException.from(cve)
        }
}
