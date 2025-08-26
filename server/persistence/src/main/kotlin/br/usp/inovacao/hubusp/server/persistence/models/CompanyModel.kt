package br.usp.inovacao.hubusp.server.persistence.models

import kotlinx.serialization.Serializable

@Serializable
data class CompanyModel(
    val active: Boolean? = true,
    val address: CompanyAddressModel,
    val allowed: Boolean? = true,
    val classification: CompanyClassificationModel,
    val cnae: String,
    val cnpj: String,
    val companySize: Set<String>,
    val corporateName: String? = "",
    val description: String,
    val ecosystems: Set<String>,
    val emails: Set<String>,
    val incubated: String,
    val logo: String? = null,
    val name: String,
    val phones: Set<String>,
    val services: Set<String>,
    val technologies: Set<String>,
    val partners: List<PartnerModel>,
    val url: String? = null,
    val year: String,
    val odss: Set<String>? = null,
    val linkedin: String? = null,
    val instagram: String? = null,
    val youtube: String? = null,
    val facebook: String? = null,
    val isUnicorn: Boolean? = null,
    val totalCollaborators: Int? = null,
    val dnaUspWanted: Boolean = false,
    val dnaUspContactEmail: String? = null,
    val dnaUspContactName: String? = null,
    val dnaUspContract: String? = null,
    val truthfulInformation: Boolean = false,
    val agreementOptions: Set<String>? = emptySet(),
    val partnerNames: List<String>? = emptyList(),
    val partnerNusps: List<String>? = emptyList(),
    val partnerBonds: List<String>? = emptyList(),
    val partnerUnities: List<String>? = emptyList(),
    val partnerPositions: List<String>? = emptyList(),
    val partnerEmails: List<String>? = emptyList(),
    val partnerPhones: List<String>? = emptyList(),
    val totalPartners: Int? = null,
    val hasUspPartners: Boolean? = null,
    val wantsToAddMorePartners: Boolean? = null,
    val cltEmployees: Int? = 0,
    val pjCollaborators: Int? = 0,
    val internsScholars: Int? = 0,
    val hasInvestment: Boolean? = false,
    val investmentTypes: Set<String>? = emptySet(),
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
data class PartnerModel(
    val name: String,
    val nusp: String? = null,
    val bond: String? = null,
    val unity: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val position: String? = null
)

@Serializable
data class CompanyClassificationModel(
    val major: String,
    val minor: String
)

@Serializable
data class CompanyAddressModel(
    val cep: String,
    val city: String,
    val neighborhood: String,
    val state: String,
    val venue: String
)
