package br.usp.inovacao.hubusp.curatorship.sheets

import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import it.skrape.fetcher.BlockingFetcher
import it.skrape.fetcher.Request
import it.skrape.fetcher.Result
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DisciplineOfferingTest {
    @MockK private lateinit var mockFetcher: BlockingFetcher<Request>

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
        every { mockFetcher.requestBuilder } returns Request()
    }

    companion object {
        fun createMockResultFromResource(resource: String) =
            Result(
                responseBody =
                    this::class.java.getResourceAsStream(resource)?.bufferedReader()?.readText()!!,
                responseStatus = Result.Status(200, "OK"),
                contentType = "text/html",
                headers = emptyMap(),
                baseUri = "",
                cookies = emptyList(),
            )
    }

    @Test
    fun `it can parse multiple offerings from Jupiter`() {
        every { mockFetcher.fetch(any()) } returns
            createMockResultFromResource(
                "/sheets/DisciplineOffering/jupiter/multipleOfferings.html",
            )

        val offerings = DisciplineOffering.trySetFromJupiter("", 10000, mockFetcher)

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
    fun `it can parse a single offering from Jupiter`() {
        every { mockFetcher.fetch(any()) } returns
            createMockResultFromResource(
                "/sheets/DisciplineOffering/jupiter/singleOffering.html",
            )

        val offerings = DisciplineOffering.trySetFromJupiter("", 10000, mockFetcher)

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

    @Test
    fun `it can parse a single offering from Janus`() {
        every { mockFetcher.fetch(any()) } returnsMany
            listOf(
                createMockResultFromResource(
                    "/sheets/DisciplineOffering/janus/Discipline.html",
                ),
                createMockResultFromResource(
                    "/sheets/DisciplineOffering/janus/Offering.html",
                ),
            )

        val offerings = DisciplineOffering.trySetFromJanus("", 10000, mockFetcher)

        assertEquals(
            setOf(
                DisciplineOffering(
                    classCode = "2",
                    startDate = "06/08/2025",
                    endDate = "12/12/2025",
                    professors = setOf("Professor 1", "Professor 2"),
                ),
            ),
            offerings,
        )
    }
}
