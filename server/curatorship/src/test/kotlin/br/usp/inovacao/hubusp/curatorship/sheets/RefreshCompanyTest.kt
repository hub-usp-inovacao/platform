package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer
import io.mockk.Called
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class RefreshCompanyTest {
    @MockK private lateinit var mailer: Mailer
    @MockK private lateinit var reader: SpreadsheetReader
    @MockK private lateinit var repo: CompanyRepository
    @MockK private lateinit var errorRepo: CompanyErrorRepository

    private lateinit var underTest: RefreshCompany

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        underTest = RefreshCompany(mailer, reader, repo, errorRepo)
    }

    @Test
    fun `it notifies developers when there's an error during reading`() {
        // given
        every { reader.read(any()) } throws SheetReadingException("", "", "Mock error")
        every { mailer.notifySpreadsheetError(any()) } returns Unit

        // when
        underTest.refresh()

        // then
        verify(exactly = 1) { mailer.notifySpreadsheetError(any()) }
    }

    @Test
    fun `it does not clear the company collection nor save any company when no row passes validation`() {
        // given
        every { reader.read(any()) } returns CompanyTestHelp.noValidRow()
        every { repo.save(any()) } returns Unit
        every { repo.deleteAll() } returns Unit
        every { errorRepo.save(any()) } returns Unit

        // when
        underTest.refresh()

        // then
        verify { repo.save(any()) wasNot Called }
        verify { repo.deleteAll() wasNot Called }
    }

    @Test
    fun `it clears company collection and store new companies when there are valid rows`() {
        // given
        every { reader.read(any()) } returns CompanyTestHelp.someValidRows()
        every { repo.save(any()) } returns Unit
        every { repo.deleteAll() } returns Unit
        every { errorRepo.save(any()) } returns Unit

        // when
        underTest.refresh()

        // then
        verify { repo.save(any()) wasNot Called }
        verify { repo.deleteAll() wasNot Called }
    }
}