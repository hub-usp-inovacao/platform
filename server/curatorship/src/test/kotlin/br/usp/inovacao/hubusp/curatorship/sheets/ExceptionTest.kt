package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.companyform.CompanyFormValidationException
import br.usp.inovacao.hubusp.curatorship.companyform.step.Step
import kotlin.test.Test
import kotlin.test.assertEquals

class ExceptionTest {
    @Test
    fun `it formats sheet reading exceptions`() {
        val exception = SheetReadingException("tab", "sheet-id", "boom")

        assertEquals("boom", exception.message)
    }

    @Test
    fun `it preserves validation messages`() {
        val exception = ValidationException(listOf("a", "b"))

        assertEquals(listOf("a", "b"), exception.messages)
        assertEquals("a|b", exception.message)
    }

    @Test
    fun `it preserves uniqueness message`() {
        val exception = UniquenessException("duplicate")

        assertEquals("duplicate", exception.message)
    }

    @Test
    fun `it preserves company form errors`() {
        val exception =
            CompanyFormValidationException(
                mapOf(Step.CompanyData to setOf("invalid cnpj")),
            )

        assertEquals(setOf("invalid cnpj"), exception.errorsPerStep[Step.CompanyData])
    }
}
