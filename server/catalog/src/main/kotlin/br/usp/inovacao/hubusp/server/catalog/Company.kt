package br.usp.inovacao.hubusp.server.catalog

@kotlinx.serialization.Serializable
data class Company(
    val active: Boolean,
    val address: CompanyAddress,
    val allowed: Boolean,
    val classification: CompanyClassification,
    val cnae: String,
    val cnpj: String,
    val companySize: Set<String>,
    val corporateName: String,
    val description: String,
    val ecosystems: Set<String>?,
    val emails: Set<String>?,
    val incubated: String,
    val logo: String?,
    val name: String,
    val phones: Set<String>?,
    val services: Set<String>?,
    val technologies: Set<String>?,
    val partners: List<Partner>,
    val url: String?,
    val year: String,
    val odss: Set<String>? = null,
    val socialMedias: List<String> = emptyList(),
    val dnaUspInfo: DnaUspInfo = DnaUspInfo(),
    val employeeInfo: EmployeeInfo = EmployeeInfo(),
    val investmentInfo: InvestmentInfo = InvestmentInfo(),
    val companyType: String? = null,
    val operationalStatus: String? = null,
    val totalPartners: Int? = null,
    val hasUspPartners: Boolean? = null,
    val incubatorName: String? = null,
    val isUnicorn: Boolean? = null,
    val agreements: Set<String> = emptySet(),
    val confirmationStatus: String? = null,
    val uspCategory: String? = null
) {
    companion object {
        val INDEXABLE_PROPERTIES = listOf("description", "name", "services", "technologies")
    }

    fun matches(term: String): Boolean = description.contains(term) ||
            name.contains(term) ||
            (services?.any { it.contains(term) } ?: false) ||
            (technologies?.any { it.contains(term) } ?: false)
}

@kotlinx.serialization.Serializable
data class CompanyClassification(
    val major: String,
    val minor: String
)

@kotlinx.serialization.Serializable
data class CompanyAddress(
    val cep: String,
    val city: String,
    val neighborhood: String,
    val state: String,
    val venue: String
)

@kotlinx.serialization.Serializable
data class Partner(
    val name: String,
    val nusp: String? = null,
    val bond: String? = null,
    val unity: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val position: String? = null
)

@kotlinx.serialization.Serializable
data class DnaUspInfo(
    val wantsSeal: Boolean = false,
    val contactEmail: String? = null,
    val contactName: String? = null,
    val contactAgreements: Set<String> = emptySet(),
    val category: String? = null,
    val confirmationStatus: String? = null
)

@kotlinx.serialization.Serializable
data class EmployeeInfo(
    val cltEmployees: Int = 0,
    val pjCollaborators: Int = 0,
    val internsScholars: Int = 0,
    val totalEmployees: Int = 0
)

@kotlinx.serialization.Serializable
data class InvestmentInfo(
    val hasInvestment: Boolean = false,
    val investmentTypes: Set<String> = emptySet(),
    val ownInvestmentAmount: String? = null,
    val angelInvestmentAmount: String? = null,
    val ventureCapitalAmount: String? = null,
    val privateEquityAmount: String? = null,
    val pipeAmount: String? = null,
    val otherInvestmentsAmount: String? = null,
    val revenue: String? = null,
    val companySizeRfb: String? = null
)