package br.usp.inovacao.hubusp.curatorship.sheets

import br.usp.inovacao.hubusp.curatorship.Mailer
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test

internal class RefreshInitiativeTest {

    @MockK
    private lateinit var mockSSReader: SpreadsheetReader

    @MockK
    private lateinit var mockMailer: Mailer

    @MockK
    private lateinit var mockInitiativesRepo: InitiativeRepository

    @MockK
    private lateinit var mockInitiativesErrorRepo: InitiativeErrorRepository

    private lateinit var underTest: RefreshInitiative

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        underTest = RefreshInitiative(mockMailer, mockSSReader, mockInitiativesRepo, mockInitiativesErrorRepo)
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
    fun `it stores valid rows as Initiatives and invalid as InitiativesError`() {
        // given
        every { mockSSReader.read(any()) } returns InitiativesTestHelp.validRowAndInvalidRow()
        every { mockMailer.notifySpreadsheetError(any()) } returns Unit
        every { mockInitiativesRepo.save(any()) } returns Unit
        every { mockInitiativesErrorRepo.save(any<InitiativeValidationError>()) } returns Unit
        every { mockInitiativesRepo.clean()} returns Unit

        // when
        underTest.refresh()

        // then
        verify(exactly = 1) { mockInitiativesRepo.save(any()) }
        verify(exactly = 1) { mockInitiativesErrorRepo.save(any<InitiativeValidationError>()) }
    }

    @Test
    fun `it cleans old data after storing valid rows`(){
        // given
        every { mockInitiativesErrorRepo.save(any<InitiativeValidationError>()) } returns Unit
        every { mockSSReader.read(any()) } returns InitiativesTestHelp.validRowAndInvalidRow()
        every { mockInitiativesRepo.save(any()) } returns Unit
        every { mockInitiativesRepo.clean() } returns Unit

        //when
        underTest.refresh()
        //then
        verify(exactly = 1) { mockInitiativesRepo.clean() }
    }
}