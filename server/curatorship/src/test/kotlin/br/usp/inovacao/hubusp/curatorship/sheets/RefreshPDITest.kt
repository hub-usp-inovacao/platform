package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class RefreshPDITest {

    @MockK
    private lateinit var mockSSReader: SpreadsheetReader

    @MockK
    private lateinit var mockMailer: Mailer

    @MockK
    private lateinit var mockPDIRepo: PDIRepository

    @MockK
    private lateinit var mockPDIErrorRepo: PDIErrorRepository

    private lateinit var underTest: RefreshPDI

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        underTest = RefreshPDI(mockMailer, mockSSReader, mockPDIRepo, mockPDIErrorRepo)
    }

    @Test
    fun `it informs if an sheet error happens` () {
        // given
        every { mockSSReader.read(any()) } throws SheetReadingException("", "", "Mock error")
        every { mockMailer.notifySpreadsheetError(any()) } returns Unit

        // when
        underTest.refresh()

        // then
        verify {
            mockMailer.notifySpreadsheetError(any())
        }
    }

    @Test
    fun `it stores valid rows as PDI and invalid as PDIError`() {
        // given
        every { mockSSReader.read(any()) } returns PDITestHelp.validRowAndInvalidRow()
        every { mockMailer.notifySpreadsheetError(any()) } returns Unit
        every { mockPDIRepo.save(any()) } returns Unit
        every { mockPDIErrorRepo.save(any()) } returns Unit
        every { mockPDIRepo.clean()} returns Unit

        // when
        underTest.refresh()

        // then
        verify(exactly = 1) { mockPDIRepo.save(any()) }
        verify(exactly = 1) { mockPDIErrorRepo.save(any()) }
    }

    @Test
    fun `it removes all the data stored in the database`(){
        // given
        every { mockSSReader.read(any()) } returns PDITestHelp.validRowAndInvalidRow()
        every { mockPDIRepo.save(any()) } returns Unit
        every { mockPDIRepo.clean() } returns Unit

        //when
        mockPDIRepo.clean()
        //then
        verify(exactly = 0) { mockPDIRepo.save(any()) }
    }
}