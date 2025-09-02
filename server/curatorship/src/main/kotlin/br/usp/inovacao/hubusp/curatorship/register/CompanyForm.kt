package br.usp.inovacao.hubusp.curatorship.register

import br.usp.inovacao.hubusp.curatorship.register.step.AboutCompanyStep
import br.usp.inovacao.hubusp.curatorship.register.step.CompanyDataStep
import br.usp.inovacao.hubusp.curatorship.register.step.DnaUspStampStep
import br.usp.inovacao.hubusp.curatorship.register.step.IncubationStep
import br.usp.inovacao.hubusp.curatorship.register.step.InvestmentStep
import br.usp.inovacao.hubusp.curatorship.register.step.PartnerStep
import br.usp.inovacao.hubusp.curatorship.register.step.RevenueStep
import br.usp.inovacao.hubusp.curatorship.register.step.StaffStep
import br.usp.inovacao.hubusp.curatorship.register.step.Step
import br.usp.inovacao.hubusp.curatorship.register.step.StepValidationException
import br.usp.inovacao.hubusp.curatorship.register.step.validate
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
        val errorMap: MutableMap<Step, List<String>> = mutableMapOf()

        // TODO: Refactor this into a hashmap (impl some sort of LazyValidate interface)

        try {
            this.partners.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.Partner, e.messages)
        }

        try {
            this.data.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.CompanyData, e.messages)
        }

        try {
            this.about.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.AboutCompany, e.messages)
        }

        try {
            this.incubation.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.Incubation, e.messages)
        }

        try {
            this.staff.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.Staff, e.messages)
        }

        try {
            this.revenue.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.Revenue, e.messages)
        }

        try {
            this.investment.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.Investment, e.messages)
        }

        try {
            this.dnaUspStamp.validate()
        } catch (e: StepValidationException) {
            errorMap.put(Step.DNAUspStamp, e.messages)
        }

        if (errorMap.isNotEmpty()) {
            throw CompanyFormValidationException(errorMap)
        }
    }
}
