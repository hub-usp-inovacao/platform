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
    private lateinit var mockSpreadsheetReader: SpreadsheetReader

    @MockK
    private lateinit var mockMailer: Mailer

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `it informs if an sheet error happens` () {
        // given
        every {
            mockSpreadsheetReader.fakeRead(any())
        } throws SheetReadingException("", "", "Mock error")
        every {
            mockMailer.notifySpreadsheetError(any())
        } returns Unit
        val underTest = RefreshPDI(
            mailer = mockMailer,
            spreadsheetReader = mockSpreadsheetReader
        )

        // when
        underTest.refresh()

        // then
        verify {
            mockMailer.notifySpreadsheetError(any())
        }

    }

}