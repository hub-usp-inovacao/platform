package br.usp.inovacao.hubusp.curatorship.companyform.step

import br.usp.inovacao.hubusp.curatorship.companyform.isNumber
import br.usp.inovacao.hubusp.curatorship.sheets.isPhone
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isEmail
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class Partner(
    val name: String?,
    val email: String?,
    val phone: String?,
    val nusp: String?,
    val bond: String?,

    /** TODO: Rename this to unit in the frontend */
    @SerialName("unity") val unit: String?,
    val role: String?,
) {
    @Throws(StepValidationException::class)
    fun validate() =
        try {
            validate(this) {
                validate(Partner::name).isNotNull().isNotBlank()
                validate(Partner::email).isNotNull().isEmail()
                validate(Partner::phone).isNotNull().isPhone()
                validate(Partner::nusp).isNotNull()
                if (nusp != null && nusp != "") {
                    validate(Partner::nusp).isNumber()
                }
                validate(Partner::bond).isNotNull().isNotBlank()
                validate(Partner::unit).isNotNull().isNotBlank()
                validate(Partner::role).isNotNull()
            }
        } catch (cve: ConstraintViolationException) {
            throw StepValidationException.from(cve)
        }
}

typealias PartnerStep = List<Partner>

fun PartnerStep.validate() {
    val errors =
        this.mapIndexedNotNull { _, value ->
            try {
                value.validate()
                null
            } catch (sve: StepValidationException) {
                sve.messages
            }
        }

    if (errors.isNotEmpty()) {
        throw StepValidationException(messages = errors.flatten())
    }
}
