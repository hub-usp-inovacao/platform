package br.usp.inovacao.hubusp.curatorship.register.step

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class IncubationStep(
    @SerialName("was_incubated") val wasIncubated: String?,
    val ecosystem: String?,
) {
    companion object {}

    @Throws(StepValidationException::class)
    fun validate() =
        try {
            validate(this) {
                validate(IncubationStep::wasIncubated).isNotNull().isNotBlank()
                if (wasIncubated()) {
                    validate(IncubationStep::ecosystem).isNotNull().isNotBlank()
                }
            }
        } catch (cve: ConstraintViolationException) {
            throw StepValidationException.from(cve)
        }

    private fun wasIncubated() = this.wasIncubated?.matches(Regex("^Sim.*$")) ?: false
}
