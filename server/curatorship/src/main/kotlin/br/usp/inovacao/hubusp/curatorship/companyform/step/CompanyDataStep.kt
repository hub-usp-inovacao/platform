package br.usp.inovacao.hubusp.curatorship.companyform.step

import br.usp.inovacao.hubusp.curatorship.companyform.isCep
import br.usp.inovacao.hubusp.curatorship.companyform.isCnae
import br.usp.inovacao.hubusp.curatorship.companyform.isCnpj
import br.usp.inovacao.hubusp.curatorship.companyform.isCompanyNature
import br.usp.inovacao.hubusp.curatorship.companyform.isPhone
import br.usp.inovacao.hubusp.curatorship.sheets.isEmail
import java.time.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.hasSize
import org.valiktor.functions.isIn
import org.valiktor.functions.isLessThanOrEqualTo
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.functions.validate
import org.valiktor.validate

@Serializable
data class CompanyDataStep(
    val cnpj: String?,
    @SerialName("public_name") val publicName: String?,
    @SerialName("corporate_name") val corporateName: String?,
    val year: String?,
    val size: String?,
    val cnae: String?,
    @SerialName("registry_status") val registryStatus: String?,
    val phones: Set<String> = emptySet(),
    val emails: Set<String> = emptySet(),
    val street: String?,
    val neighborhood: String?,
    val city: String?,
    val state: String?,
    val zipcode: String?,
    val category: String?,
    @SerialName("company_nature") val companyNature: String?
) {
    companion object {
        val VALID_SIZES = listOf("MEI", "ME", "EPP", "DEMAIS")
        val VALID_REGISTRY_STATUS =
            listOf("Ativa", "Ativa Não Regular", "Baixada", "Inapta", "Nula", "Suspensa")

        val VALID_CATEGORIES =
            listOf(
                "Fundada por aluno ou ex-aluno de graduação da USP",
                "Fundada por aluno ou ex-aluno de pós-graduação da USP",
                "Fundada por aluno ou ex-aluno de pós-doutorado da USP",
                "Fundada por aluno ou ex-aluno do IPEN (Instituto de Pesquisas Energéticas e Nucleares)",
                "Fundada por um docente ou Professor Sênior da USP",
                "Fundada por um servidor técnico e administrativo (ou ex-servidor) da USP",
                "Empresa incubada ou graduada em uma das incubadoras ligadas à USP (Incubadora USP/IPEN, ESALQTEC, HABITS, SUPERA e ParqTec)",
            )
    }

    @Throws(StepValidationException::class)
    fun validate() =
        try {
            validate(this) {
                validate(CompanyDataStep::cnpj).isNotNull().isNotBlank().isCnpj()
                validate(CompanyDataStep::publicName).isNotNull().isNotBlank()
                validate(CompanyDataStep::corporateName).isNotNull().isNotBlank()
                validate(CompanyDataStep::year)
                    .isNotNull()
                    .hasSize(4)
                    .isLessThanOrEqualTo(LocalDate.now().year.toString())
                validate(CompanyDataStep::size).isNotNull().isIn(VALID_SIZES)
                validate(CompanyDataStep::cnae).isNotNull().isNotBlank().isCnae()
                validate(CompanyDataStep::registryStatus).isNotNull().isIn(VALID_REGISTRY_STATUS)
                validate(CompanyDataStep::phones).isNotNull().isPhone()
                validate(CompanyDataStep::emails).isNotNull().isEmail()

                validate(CompanyDataStep::street).isNotNull().isNotBlank()
                validate(CompanyDataStep::city).isNotNull().isNotBlank()
                validate(CompanyDataStep::state).isNotNull().isNotBlank()
                validate(CompanyDataStep::zipcode).isNotNull().isNotBlank().isCep()

                validate(CompanyDataStep::category).isNotNull().isIn(VALID_CATEGORIES)
                validate(CompanyDataStep::companyNature).isNotNull().isNotBlank().isCompanyNature()
            }
        } catch (cve: ConstraintViolationException) {
            throw StepValidationException.from(cve)
        }
}
