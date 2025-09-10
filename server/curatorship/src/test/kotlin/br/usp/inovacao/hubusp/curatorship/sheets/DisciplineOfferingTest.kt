package br.usp.inovacao.hubusp.curatorship.sheets

import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result
import it.skrape.selects.html5.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

internal class DisciplineOfferingTest {
    private class MockFetcher(val mockedResponse: String) : BlockingFetcher<Request> {
        override val requestBuilder: Request
            get() = Request()

        override fun fetch(request: Request) =
            Result(
                responseBody = this.mockedResponse,
                responseStatus = Result.Status(200, "OK"),
                contentType = "text/html",
                headers = emptyMap(),
                baseUri = "",
                cookies = emptyList(),
            )
    }

    @Test
    fun `it can parse HTML to multiple offerings`() {
        val htmlText =
            this::class
                .java
                .getResourceAsStream("/sheets/DisciplineOffering/jupiter/multipleOfferings.html")
                ?.bufferedReader()
                ?.readText()

        assertNotNull(htmlText)

        val offerings = DisciplineOffering.trySetFromJupiter("", 10000, MockFetcher(htmlText))

        assertEquals(
            setOf(
                DisciplineOffering(
                    classCode = "2025203",
                    startDate = "04/08/2025",
                    endDate = "30/09/2025",
                    professors = setOf("Docente 1"),
                ),
                DisciplineOffering(
                    classCode = "2025204",
                    startDate = "04/08/2025",
                    endDate = "30/09/2025",
                    professors = setOf("Docente 2"),
                ),
            ),
            offerings,
        )
    }

    @Test
    fun `it can parse HTML to a valid offering`() {
        val htmlText =
            this::class
                .java
                .getResourceAsStream("/sheets/DisciplineOffering/jupiter/singleOffering.html")
                ?.bufferedReader()
                ?.readText()

        assertNotNull(htmlText)

        val offerings = DisciplineOffering.trySetFromJupiter("", 10000, MockFetcher(htmlText))

        assertEquals(
            setOf(
                DisciplineOffering(
                    classCode = "20252CC",
                    startDate = "04/08/2025",
                    endDate = "12/12/2025",
                    professors =
                        setOf(
                            "Docente 1",
                            "Docente 2",
                            "Docente 3",
                        ),
                ),
            ),
            offerings,
        )
    }
}
