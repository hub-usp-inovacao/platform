package br.usp.inovacao.hubusp.curatorship.register.step

import br.usp.inovacao.hubusp.curatorship.register.isBrl
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotEmpty
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class InvestmentStep(
    /** Sim / Não */
    val received: String?,

    /** List of investments received ("Investimento próprio", "Investimento-anjo", etc) */
    @SerialName("investments") val investmentsReceived: Set<String> = emptySet(),

    /** "Investimento próprio" */
    val own: String?,

    /** "Investimento-anjo" */
    val angel: String?,

    /** "Venture capital" */
    val venture: String?,

    /** "Private equity" */
    val equity: String?,

    /** "PIPE-FAPESP" */
    val pipe: String?,

    /** "Crowdfunding" / "BNDES e/ou FINEP" */
    val others: String?
) {
    companion object {}

    @Throws(StepValidationException::class)
    fun validate() =
        try {
            validate(this) {
                if (receivedAnyInvestment()) {
                    validate(InvestmentStep::investmentsReceived).isNotEmpty()
                    validate(InvestmentStep::own).isNotNull().isBrl()
                    validate(InvestmentStep::angel).isNotNull().isBrl()
                    validate(InvestmentStep::venture).isNotNull().isBrl()
                    validate(InvestmentStep::equity).isNotNull().isBrl()
                    validate(InvestmentStep::pipe).isNotNull().isBrl()
                    validate(InvestmentStep::others).isNotNull().isBrl()
                }
            }
        } catch (cve: ConstraintViolationException) {
            throw StepValidationException.from(cve)
        }

    private fun receivedAnyInvestment() = this.received == "Sim"
}
