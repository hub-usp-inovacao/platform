package br.usp.inovacao.hubusp.curatorship.companyform.step

import br.usp.inovacao.hubusp.curatorship.companyform.isBrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class RevenueStep(
    @SerialName("last_year") val lastYearRevenue: String?,
) {
    companion object {}

    @Throws(StepValidationException::class)
    fun validate() =
        try {
            validate(this) { validate(RevenueStep::lastYearRevenue).isNotNull().isBrl() }
        } catch (cve: ConstraintViolationException) {
            throw StepValidationException.from(cve)
        }
}
