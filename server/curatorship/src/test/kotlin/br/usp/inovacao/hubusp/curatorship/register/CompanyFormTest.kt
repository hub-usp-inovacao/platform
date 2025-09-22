package br.usp.inovacao.hubusp.curatorship.register

import br.usp.inovacao.hubusp.curatorship.register.step.AboutCompanyStepTest
import br.usp.inovacao.hubusp.curatorship.register.step.CompanyDataStepTest
import br.usp.inovacao.hubusp.curatorship.register.step.DnaUspStampStepTest
import br.usp.inovacao.hubusp.curatorship.register.step.IncubationStepTest
import br.usp.inovacao.hubusp.curatorship.register.step.InvestmentStepTest
import br.usp.inovacao.hubusp.curatorship.register.step.PartnerStepTest
import br.usp.inovacao.hubusp.curatorship.register.step.RevenueStepTest
import br.usp.inovacao.hubusp.curatorship.register.step.StaffStepTest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class CompanyFormTest {
    private fun createValidCompanyForm() =
        CompanyForm(
            partners = PartnerStepTest.VALID_STEP,
            data = CompanyDataStepTest.VALID_STEP,
            about = AboutCompanyStepTest.VALID_STEP,
            incubation = IncubationStepTest.VALID_STEP,
            staff = StaffStepTest.VALID_STEP,
            revenue = RevenueStepTest.VALID_STEP,
            investment = InvestmentStepTest.VALID_STEP,
            dnaUspStamp = DnaUspStampStepTest.VALID_STEP,
        )

    @Test
    fun `it deserializes a valid company form from JSON`() {
        Json.decodeFromString<CompanyForm>(validCompanyFormJSON)
    }

    @Test
    fun `it does not throws an error when company form is valid`() {
        createValidCompanyForm()
    }

    @Test
    fun `it throws an error when company form has invalid partner step`() {
        assertFailsWith<CompanyFormValidationException> {
            createValidCompanyForm().copy(partners = PartnerStepTest.INVALID_STEP)
        }
    }

    @Test
    fun `it throws an error when company form has invalid company data step`() {
        assertFailsWith<CompanyFormValidationException> {
            createValidCompanyForm().copy(data = CompanyDataStepTest.INVALID_STEP)
        }
    }

    @Test
    fun `it throws an error when company form has invalid about company step`() {
        assertFailsWith<CompanyFormValidationException> {
            createValidCompanyForm().copy(about = AboutCompanyStepTest.INVALID_STEP)
        }
    }

    @Test
    fun `it throws an error when company form has invalid incubation step`() {
        assertFailsWith<CompanyFormValidationException> {
            createValidCompanyForm().copy(incubation = IncubationStepTest.INVALID_STEP)
        }
    }

    @Test
    fun `it throws an error when company form has invalid staff step`() {
        assertFailsWith<CompanyFormValidationException> {
            createValidCompanyForm().copy(staff = StaffStepTest.INVALID_STEP)
        }
    }

    @Test
    fun `it throws an error when company form has invalid revenue step`() {
        assertFailsWith<CompanyFormValidationException> {
            createValidCompanyForm().copy(revenue = RevenueStepTest.INVALID_STEP)
        }
    }

    @Test
    fun `it throws an error when company form has invalid investment step`() {
        assertFailsWith<CompanyFormValidationException> {
            createValidCompanyForm().copy(investment = InvestmentStepTest.INVALID_STEP)
        }
    }

    @Test
    fun `it throws an error when company form has invalid dna usp stamp step`() {
        assertFailsWith<CompanyFormValidationException> {
            createValidCompanyForm().copy(dnaUspStamp = DnaUspStampStepTest.INVALID_STEP)
        }
    }

    private val validCompanyFormJSON =
        """
{
  "partners": [
    {
      "name": "Some partner",
      "email": "test@example.com",
      "phone": "(11) 99999-9999",
      "nusp": "1234567",
      "bond": "Some bond",
      "unity": "Some unit",
      "role": "Some role"
    }
  ],
  "company_data": {
    "cnpj": "00.000.000/0001-00",
    "public_name": "Some public name",
    "corporate_name": "Some corporate name",
    "year": "2025",
    "size": "MEI",
    "cnae": "00.00-0-00",
    "registry_status": "Ativa",
    "phones": [
      "(11) 99999-4433"
    ],
    "emails": [
      "test@example.com"
    ],
    "street": "Rua tal",
    "neighborhood": "Vila tal",
    "city": "Cidade tal",
    "state": "Estado tal",
    "zipcode": "00000-000",
    "category": "Fundada por aluno ou ex-aluno de graduação da USP",
    "company_nature": "000-0 - Automated Tests"
  },
  "about_company": {
    "description": "Empresa para testes",
    "logo": "https://www.logodofulanodetal.com",
    "services": [
      "Website",
      "Blog"
    ],
    "technologies": [
      "Aplicativos",
      "Biomateriais"
    ],
    "site": "https://www.fulanodetal.com",
    "odss": [
      "1 - Erradicação da Pobreza"
    ],
    "social_medias": [
      "https://www.fulanodetal.com"
    ]
  },
  "incubation": {
    "was_incubated": "Sim. Something",
    "ecosystem": "Something"
  },
  "staff": {
    "number_of_CLT_employees": "00",
    "number_of_PJ_colaborators": "00",
    "number_of_interns": "00"
  },
  "revenue": {
    "last_year": "R$ 1.234.567,89"
  },
  "investment": {
    "received": "Sim",
    "investments": [
      "Investmento próprio"
    ],
    "own": "R$ 1,00",
    "angel": "R$ 0,00",
    "venture": "R$ 0,00",
    "equity": "R$ 0,00",
    "pipe": "R$ 0,00",
    "others": "R$ 0,00"
  },
  "dna_usp_stamp": {
    "wants": true,
    "name": "fulano de tal",
    "email": "fulano@mail.com",
    "truthful_informations": true,
    "permissions": [
      "Permito o envio de e-mails para ser avisado sobre eventos e oportunidades relevantes à empresa",
      "Permito a divulgação das informações públicas na plataforma Hub USPInovação",
      "Permito a divulgação das informações públicas na plataforma Hub USPInovação e também para unidades da USP"
    ]
  }
}
    """
}
