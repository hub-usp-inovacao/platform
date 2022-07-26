package br.usp.inovacao.hubusp.curatorship

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertIs
import kotlin.test.assertNull

internal class RefreshPDITest {
    private lateinit var underTest: RefreshPDI

    @MockK
    private lateinit var pdiRepository: PDIRepository

    @MockK
    private lateinit var pdiSpreadsheetReader: PDISpreadsheetReader

    @MockK
    private lateinit var validationErrorRepository: ValidationErrorRepository

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        underTest = RefreshPDI(
            pdiRepository = this.pdiRepository,
            pdiSpreadsheetReader = this.pdiSpreadsheetReader,
            validationErrorRepository = this.validationErrorRepository
        )
    }

    @Test
    fun `it can fetch data using PDISpreadsheetReader` () {
        // given
        every { pdiSpreadsheetReader.fetch() } returns emptyList()

        // when
        underTest.fetchData()

        // then
        verify { pdiSpreadsheetReader.fetch() }
    }

    @Test
    fun `it can detect validation errors` () {
        // given
        val rawPDI = listOf("","CEPID")

        // when
        val pair: Pair<PDI?, ValidationError?> = underTest.validate(rawPDI)

        //then
        assertNull(pair.first)
        assertIs<ValidationError>(pair.second)
    }
}