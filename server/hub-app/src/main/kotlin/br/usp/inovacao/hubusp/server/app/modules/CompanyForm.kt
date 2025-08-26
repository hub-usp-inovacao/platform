package br.usp.inovacao.hubusp.server.app.modules

import kotlinx.serialization.Serializable

@Serializable
data class CompanyForm(
    val cnpj: String,
    val name: String,
    val corporateName: String,
    val year: String,
    val cnae: String,
    val phones: List<String>,
    val emails: List<String>,
    val address: Address,
    val description: String,
    val services: List<String>,
    val technologies: List<String>,
    val logo: String? = null,
    val url: String? = null,
    val incubated: String,
    val ecosystems: List<String>,
    val isUnicorn: Boolean? = null,
    val totalCollaborators: Int? = null,
    val odss: List<String>? = null,
    val linkedin: String? = null,
    val instagram: String? = null,
    val youtube: String? = null,
    val facebook: String? = null,
    val dnaUspWanted: Boolean = false,
    val dnaUspContactEmail: String? = null,
    val dnaUspContactName: String? = null,
    val dnaUspContract: String? = null,
    val truthfulInformation: Boolean = false,
    val agreementOptions: List<String> = emptyList(),
    val partners: List<PartnerForm>? = null, // Manter compatibilidade com estrutura antiga
    val partnerNames: List<String> = emptyList(),
    val partnerNusps: List<String> = emptyList(),
    val partnerBonds: List<String> = emptyList(),
    val partnerUnities: List<String> = emptyList(),
    val partnerPositions: List<String> = emptyList(),
    val partnerEmails: List<String> = emptyList(),
    val partnerPhones: List<String> = emptyList(),
    val totalPartners: Int? = null,
    val hasUspPartners: Boolean? = null,
    val wantsToAddMorePartners: Boolean? = null,
    val cltEmployees: Int = 0,
    val pjCollaborators: Int = 0,
    val internsScholars: Int = 0,
    val hasInvestment: Boolean = false,
    val investmentTypes: List<String> = emptyList(),
    val ownInvestmentAmount: String? = null,
    val angelInvestmentAmount: String? = null,
    val ventureCapitalAmount: String? = null,
    val privateEquityAmount: String? = null,
    val pipeAmount: String? = null,
    val otherInvestmentsAmount: String? = null,
    val revenue2022: String? = null,
    val rfbSize: String? = null,
    val totalSum: Int? = null,
    val companyType: String? = null,
    val operationalStatus: String? = null,
    val index: String? = null,
    val incubatorBond: String? = null,
    val uspBondConfirmation: String? = null,
    val uspDnaCategory: String? = null,
    val companyBondConfirmation: String? = null
)

@Serializable
data class Address(
    val venue: String,
    val neighborhood: String,
    val city: String,
    val state: String,
    val cep: String
)

@Serializable
data class PartnerForm(
    val name: String,
    val nusp: String? = null,
    val bond: String? = null,
    val unity: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val position: String? = null
)

@Serializable
data class DnaUspInfoForm(
    val wantsSeal: Boolean = false,
    val contactEmail: String? = null,
    val contactName: String? = null,
    val contactAgreements: List<String> = emptyList(),
    val category: String? = null,
    val confirmationStatus: String? = null
)

@Serializable
data class EmployeeInfoForm(
    val cltEmployees: Int = 0,
    val pjCollaborators: Int = 0,
    val internsScholars: Int = 0,
    val totalEmployees: Int = 0
)

@Serializable
data class InvestmentInfoForm(
    val hasInvestment: Boolean = false,
    val investmentTypes: List<String> = emptyList(),
    val ownInvestmentAmount: String? = null,
    val angelInvestmentAmount: String? = null,
    val ventureCapitalAmount: String? = null,
    val privateEquityAmount: String? = null,
    val pipeAmount: String? = null,
    val otherInvestmentsAmount: String? = null,
    val revenue: String? = null,
    val companySize: String? = null
)
