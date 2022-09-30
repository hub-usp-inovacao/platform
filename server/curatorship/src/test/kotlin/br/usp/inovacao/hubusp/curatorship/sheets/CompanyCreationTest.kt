package br.usp.inovacao.hubusp.curatorship.sheets

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

internal class CompanyCreationTest {
    private val valid: Company = CompanyTestHelp.validSample.copy()

    @Test
    fun `it blocks null cnpj`() {
        // given
        val nullCnpj = null

        // when ... then
        assertFailsWith<ValidationException> {
            valid.copy(cnpj = nullCnpj)
        }
    }

    @Test
    fun `it blocks malformed cnpj`() {
        // given
        val malformedCnpj = "not-cnpj-here"

        // when ... then
        assertFailsWith<ValidationException> {
            valid.copy(cnpj = malformedCnpj)
        }
    }

    @Test
    fun `it passes with a correctly formed cnpj`() {
        // when
        val result = valid.copy()

        // then
        assertIs<Company>(result)
    }

    @Test
    fun `it reads CNPJ from sheet row`() {
        // given
        val row = CompanyTestHelp.singleValidRow()

        // when
        val result = Company.fromRow(row)

        // then
        assertIs<Company>(result)
    }
}