package br.usp.inovacao.hubusp.curatorship.register

import br.usp.inovacao.hubusp.curatorship.register.step.AboutCompanyStep
import br.usp.inovacao.hubusp.curatorship.register.step.CompanyDataStep

// TODO: toJSON

fun AboutCompanyStep.toRow(): List<String?> =
    listOf(
        description,
        logo,
        services.joinToString(";"),
        technologies.joinToString(";"),
        site,
        odss.joinToString(";"),
        socialMedias.joinToString(";"),
    )

fun CompanyForm.toRow(): List<String?> =
    listOf(
        about.description,
        about.logo,
        about.services.joinToString(";"),
        about.technologies.joinToString(";"),
        about.site,
        about.odss.joinToString(";"),
        about.socialMedias.joinToString(";"),
    )

class CompanyFormTestHelp {

    companion object {

        val VALID_ABOUT =
            AboutCompanyStep(
                description = "Empresa para testes",
                logo = "https://www.logodofulanodetal.com",
                services = setOf("Website", "Blog"),
                technologies = setOf("Aplicativos", "Biomateriais"),
                site = "https://www.fulanodetal.com",
                odss = setOf("1 - Erradicação da Pobreza"),
                socialMedias = setOf("https://www.fulanodetal.com"),
            )

        val VALID_COMPANY_DATA =
            CompanyDataStep(
                cnpj = "00.000.000/0001-00",
                publicName = "Some public name",
                corporateName = "Some corporate name",
                year = "2025",
                size = "MEI",
                cnae = "00.00-0-00",
                registryStatus = "Ativa",
                phones = setOf("(11) 99999-4433"),
                emails = setOf("test@example.com"),
                street = "Rua tal",
                neighborhood = "Vila tal",
                city = "Cidade tal",
                state = "Estado tal",
                zipcode = "00000-000",
                companyNature = "000-0 - Automated Tests",
            )

        val VALID_REGISTER_RECORD = CompanyForm(about = VALID_ABOUT)

        fun validRegisterAndInvalidRegister(): List<List<String?>> {
            val validRegister = VALID_REGISTER_RECORD.toRow()
            val invalidRegister = VALID_REGISTER_RECORD.toRow().toMutableList()
            invalidRegister[0] = ""
            return listOf(validRegister, invalidRegister)
        }

        val validRegister: List<String?> = VALID_REGISTER_RECORD.toRow()
    }
}
