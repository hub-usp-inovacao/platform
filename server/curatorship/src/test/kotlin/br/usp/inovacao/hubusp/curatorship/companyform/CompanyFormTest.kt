package br.usp.inovacao.hubusp.curatorship.companyform

import br.usp.inovacao.hubusp.curatorship.companyform.step.AboutCompanyStepTest
import br.usp.inovacao.hubusp.curatorship.companyform.step.CompanyDataStepTest
import br.usp.inovacao.hubusp.curatorship.companyform.step.DnaUspStampStepTest
import br.usp.inovacao.hubusp.curatorship.companyform.step.IncubationStepTest
import br.usp.inovacao.hubusp.curatorship.companyform.step.InvestmentStepTest
import br.usp.inovacao.hubusp.curatorship.companyform.step.PartnerStepTest
import br.usp.inovacao.hubusp.curatorship.companyform.step.RevenueStepTest
import br.usp.inovacao.hubusp.curatorship.companyform.step.StaffStepTest
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
        val validCompanyFormJSON =
            this::class
                .java
                .getResourceAsStream("/companyform/validCompanyForm.json")
                ?.bufferedReader()
                ?.readText()!!

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
}
