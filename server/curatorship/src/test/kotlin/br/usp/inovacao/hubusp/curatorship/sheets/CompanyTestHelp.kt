package br.usp.inovacao.hubusp.curatorship.sheets

import java.time.LocalDateTime

fun CompanyAddress.toRow(): List<String?> = listOf(
    venue,
    neighborhood,
    city,
    state,
    cep
)

fun Partner.toRow(): List<String?> = listOf(
    name,
    nusp,
    bond,
    unity,
    email,
    phone
)

fun CompanyClassification.toRow(): List<String?> = listOf(
    major,
    minor
)

fun Company.toRow(): List<String?> {
    val lead = listOf(
        null,
        cnpj,
        name,
        corporateName,
        year,
        cnae,
        phones?.joinToString(";"),
        emails?.joinToString(";"),
        address.venue,
        address.neighborhood,
        address.city,
        address.state,
        address.cep,
        description,
        services?.joinToString(";"),
        technologies?.joinToString(";"),
        logo,
        url,
        incubated,
        ecosystems?.joinToString(";")
    )

    val middleOffset = MutableList(13) { null }

    val firstPartner: List<String?>
    if (partners!!.size > 0) {
        firstPartner = listOf(
            partners?.elementAt(0)?.name,
            partners?.elementAt(0)?.nusp,
            partners?.elementAt(0)?.bond,
            partners?.elementAt(0)?.unity,
            null,
            partners?.elementAt(0)?.email,
            partners?.elementAt(0)?.phone
        )
    } else {
        firstPartner = MutableList(7) { null }
    }

    val partnersOffset = MutableList(3) { null }

    val otherPartners = mutableListOf<String?>()
    for (i in 1..4) {
        if (partners!!.size <= i) {
            otherPartners.addAll(
                listOf(
                    null,
                    null,
                    null,
                    null,
                    null
                )
            )
        } else {
            otherPartners.addAll(
                listOf(
                    partners?.elementAt(i)?.name,
                    partners?.elementAt(i)?.nusp,
                    partners?.elementAt(i)?.bond,
                    partners?.elementAt(i)?.unity,
                    null
                )
            )
        }
    }

    val tail = MutableList(20) { null }

    return lead + middleOffset + firstPartner + partnersOffset + otherPartners + tail
}

class CompanyTestHelp {
    companion object {
        val VALID_ADDRESS_RECORD = CompanyAddress(
            cep = "13414-157",
            city = "Piracicaba",
            neighborhood = "Loteamento Santa Rosa",
            state = "São Paulo",
            venue = "Rua Cezira Giovanni"
        )

        val VALID_PARTNER_RECORD = Partner(
            name = "Fulano de Tal",
            nusp = "1234567",
            bond = "Pesquisador",
            unity = "Faculdade de Odontologia de Bauru - FOB",
            email = "fulano@detal.com",
            phone = "(11) 99999-9999"
        )

        val VALID_CLASSIFICATION_RECORD = CompanyClassification(
            major = "Comércio e Serviços",
            minor = "Informação e Comunicação"
        )

        val VALID_COMPANY_RECORD = Company(
            cnpj = "12.123.123/0001-21",
            name = "Uber 99",
            corporateName = "razão social",
            year = "2019",
            services = setOf("foo", "bar", "baz"),
            incubated = "Não",
            emails = setOf("foo@exmaple.com", "bar@exmaple.com"),
            ecosystems = setOf("ESALQTec"),
            description = "foo bar baz",
            allowed = true,
            address = VALID_ADDRESS_RECORD,
            active = true,
            url = "https://techagr.com",
            technologies = setOf("foo bar baz"),
            phones = setOf("(11) 987288877"),
            logo = "https://drive.google.com/...",
            companySize = setOf("Média Empresa"),
            cnae = "66.13-4-00",
            classification = VALID_CLASSIFICATION_RECORD,
            partners = listOf(VALID_PARTNER_RECORD)
        )

        fun validRowAndInvalidRow(): List<List<String?>> {
            val header = listOf(
                "TIME",
                "CNPJ",
                "Nome Fantasia",
                "Razão Social",
                "Ano de Fundação",
                "CNAE",
                "Telefone",
                "E-mail",
                "Endereço",
                "Bairro",
                "Cidade",
                "Estado",
                "CEP",
                "Descrição",
                "Serviços",
                "Tecnologias",
                "Logo",
                "URL",
                "Incubada",
                "Ecossistemas",
                "Unicórnio",
                "Número de Colaboradores",
                "ODS",
                "Linkedin",
                "Instagram",
                "Youtube",
                "Facebook",
                "Selo DNA USP",
                "Email DNA USP",
                "Responsável email DNA USP",
                "Contrato de uso da marca DNA USP",
                "Declaro que as informações são verdadeiras",
                "Opções que a empresa está de acordo",
                "Sócio 1 - Nome",
                "Sócio 1 - NUSP",
                "Sócio 1 - Vínculo",
                "Sócio 1 - Unidade",
                "Sócio 1 - Cargo",
                "Sócio 1 - E-mail",
                "Sócio 1 - Telefone",
                "Quantos sócios a empresa possui",
                "Possui mais sócios que mantêm vínculo com a USP",
                "Gostaria de adicionar os demais sócios",
                "Sócio 2 - Nome",
                "Sócio 2 - NUSP",
                "Sócio 2 - Vínculo",
                "Sócio 2 - Unidade",
                "Deseja adicionar mais sócios",
                "Sócio 3 - Nome",
                "Sócio 3 - NUSP",
                "Sócio 3 - Vínculo",
                "Sócio 3 - Unidade",
                "Deseja adicionar mais sócios",
                "Sócio 4 - Nome",
                "Sócio 4 - NUSP",
                "Sócio 4 - Vínculo",
                "Sócio 4 - Unidade",
                "Deseja adicionar mais sócios",
                "Sócio 5 - Nome",
                "Sócio 5 - NUSP",
                "Sócio 5 - Vínculo",
                "Sócio 5 - Unidade",
                "Número de funcionários contratados como CLT",
                "Número de colaboradores contratados",
                "Número de estagiários e bolsistas",
                "A empresa recebeu investimento?",
                "Quais investimentos a empresa recebeu?",
                "Valor do investimento próprio",
                "Valor do investimento-anjo",
                "Valor do venture capital",
                "Valor do private equity",
                "Valor do PIPE-FAPESP",
                "Outros investimentos",
                "Faturamento da empresa em 2022",
                "Porte (RFB)",
                "Somatório (sócios + CLT + PJ + E/B)",
                "Tipo de empresa",
                "Status operacional da empresa",
                "Índice",
                "Vínculo com a incubadora",
                "Confirmação",
                "Em qual categoria de DNA USP a empresa se encontra",
                "Confirmação de vínculo EMPRESA"
            )
            val validRow = VALID_COMPANY_RECORD.toRow()
            val invalidRow = VALID_COMPANY_RECORD.toRow().toMutableList()
            invalidRow[1] = "00.000.000.0000-11"

            return listOf(header, validRow, invalidRow)
        }

        val validRow: List<String?> = VALID_COMPANY_RECORD.toRow()
    }
}
