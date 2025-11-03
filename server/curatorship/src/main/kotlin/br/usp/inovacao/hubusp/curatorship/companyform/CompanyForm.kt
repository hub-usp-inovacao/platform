package br.usp.inovacao.hubusp.curatorship.companyform

import br.usp.inovacao.hubusp.curatorship.companyform.step.AboutCompanyStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.CompanyDataStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.DnaUspStampStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.IncubationStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.InvestmentStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.PartnerStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.RevenueStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.StaffStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.Step
import br.usp.inovacao.hubusp.curatorship.companyform.step.StepValidationException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompanyForm(
    val partners: PartnerStep,
    @SerialName("company_data") val data: CompanyDataStep,
    @SerialName("about_company") val about: AboutCompanyStep,
    val incubation: IncubationStep,
    val staff: StaffStep,
    val revenue: RevenueStep,
    val investment: InvestmentStep,
    @SerialName("dna_usp_stamp") val dnaUspStamp: DnaUspStampStep,
) {
    init {
        val errorMap: MutableMap<Step, MutableSet<String>> = mutableMapOf()

        val steps =
            listOf(
                Pair(Step.CompanyData, this.data),
                Pair(Step.Investment, this.investment),
                Pair(Step.Revenue, this.revenue),
                Pair(Step.Incubation, this.incubation),
                Pair(Step.DNAUspStamp, this.dnaUspStamp),
                Pair(Step.Staff, this.staff),
                Pair(Step.AboutCompany, this.about),
            ) + this.partners.map { Pair(Step.Partner, it) }

        for (step in steps) {
            val (stepKey, stepData) = step

            try {
                stepData.validate()
            } catch (e: StepValidationException) {
                if (errorMap.containsKey(stepKey)) {
                    errorMap.get(stepKey)?.addAll(e.messages)
                } else {
                    errorMap.put(stepKey, e.messages.toMutableSet())
                }
            }
        }

        if (errorMap.isNotEmpty()) {
            throw CompanyFormValidationException(errorMap)
        }
    }
}
