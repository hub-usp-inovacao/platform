package br.usp.inovacao.hubusp.curatorship.register

fun AboutCompanyStep.toRow(): List<String?> = listOf(
    description,
    logo,
    services.joinToString(";"),
    technologies.joinToString(";"),
    site,
    odss.joinToString(";"),
    socialMedia.joinToString(";")
)

fun CompanyStep.toRow(): List<String?> = listOf(
    about.description,
    about.logo,
    about.services.joinToString(";"),
    about.technologies.joinToString(";"),
    about.site,
    about.odss.joinToString(";"),
    about.socialMedia.joinToString(";")
)

class CompanyStepTestHelp {

    companion object {

        val VALID_ABOUT = AboutCompanyStep(
            description = "Empresa para testes",
            logo = "https://www.logodofulanodetal.com",
            services = setOf("Website","Blog"),
            technologies = setOf("Aplicativos","Biomateriais"),
            site = "https://www.fulanodetal.com",
            odss = setOf("foo","bar"),
            socialMedia = setOf("https://www.fulanodetal.com")
        )

        val VALID_REGISTER_RECORD = CompanyStep(
            about = VALID_ABOUT
        )

        fun validRegisterAndInvalidRegister(): List<List<String?>> {
            val validRegister = VALID_REGISTER_RECORD.toRow()
            val invalidRegister = VALID_REGISTER_RECORD.toRow().toMutableList()
            invalidRegister[0] = ""
            return listOf(validRegister, invalidRegister)
        }
        val validRegister: List<String?> = VALID_REGISTER_RECORD.toRow()
    }

}