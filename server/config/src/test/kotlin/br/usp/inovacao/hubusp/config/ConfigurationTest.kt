package br.usp.inovacao.hubusp.config

import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigurationTest {
    @Test
    fun `it reads api key config`() {
        assertEquals("sheets api key", Configuration.sheets.apiKey)
    }

    @Test
    fun `it reads spreadsheets config`() {
        assertEquals("company id", Configuration.sheets.company.id)
        assertEquals("company tab", Configuration.sheets.company.tab)

        assertEquals("discipline id", Configuration.sheets.discipline.id)
        assertEquals("discipline tab", Configuration.sheets.discipline.tab)

        assertEquals("funds id", Configuration.sheets.funds.id)
        assertEquals("funds tab", Configuration.sheets.funds.tab)

        assertEquals("incubators id", Configuration.sheets.incubators.id)
        assertEquals("incubators tab", Configuration.sheets.incubators.tab)

        assertEquals("initiative id", Configuration.sheets.initiative.id)
        assertEquals("initiative tab", Configuration.sheets.initiative.tab)

        assertEquals("patent id", Configuration.sheets.patent.id)
        assertEquals("patent tab", Configuration.sheets.patent.tab)

        assertEquals("pdi id", Configuration.sheets.pdi.id)
        assertEquals("pdi tab", Configuration.sheets.pdi.tab)

        assertEquals("researcher id", Configuration.sheets.researcher.id)
        assertEquals("researcher tab", Configuration.sheets.researcher.tab)

        assertEquals("company register form id", Configuration.sheets.companyRegisterForm.id)
        assertEquals("company register form tab", Configuration.sheets.companyRegisterForm.tab)
    }

    @Test
    fun `it reads email config`() {
        assertEquals("email devs", Configuration.email.devs)
        assertEquals("email username", Configuration.email.username)
        assertEquals("email password", Configuration.email.password)
        // TODO: Test (B)CC
    }

    @Test
    fun `it reads ktor config`() {
        assertEquals("allowed hosts", Configuration.ktor.allowedHosts)
    }

    @Test
    fun `it reads jwt config`() {
        assertEquals("jwt secret", Configuration.jwt.secret)
        assertEquals("jwt issuer", Configuration.jwt.issuer)
        assertEquals("jwt domain", Configuration.jwt.domain)
        assertEquals("jwt audience", Configuration.jwt.audience)
        assertEquals("jwt realm", Configuration.jwt.realm)
    }
}
