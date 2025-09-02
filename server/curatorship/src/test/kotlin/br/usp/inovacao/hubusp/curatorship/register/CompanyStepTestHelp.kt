package br.usp.inovacao.hubusp.curatorship.register

import br.usp.inovacao.hubusp.curatorship.register.step.AboutCompanyStep

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

fun CompanyStep.toRow(): List<String?> =
    listOf(
        about.description,
        about.logo,
        about.services.joinToString(";"),
        about.technologies.joinToString(";"),
        about.site,
        about.odss.joinToString(";"),
        about.socialMedias.joinToString(";"),
    )

class CompanyStepTestHelp {

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

        val VALID_REGISTER_RECORD = CompanyStep(about = VALID_ABOUT)

        fun validRegisterAndInvalidRegister(): List<List<String?>> {
            val validRegister = VALID_REGISTER_RECORD.toRow()
            val invalidRegister = VALID_REGISTER_RECORD.toRow().toMutableList()
            invalidRegister[0] = ""
            return listOf(validRegister, invalidRegister)
        }

        val validRegister: List<String?> = VALID_REGISTER_RECORD.toRow()
    }
}
