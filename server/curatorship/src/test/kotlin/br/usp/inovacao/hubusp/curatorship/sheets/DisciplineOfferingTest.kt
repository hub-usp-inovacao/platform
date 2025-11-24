package br.usp.inovacao.hubusp.curatorship.sheets

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlin.test.Test
import kotlin.test.assertEquals

internal class DisciplineOfferingTest {
    companion object {
        fun getResource(path: String) =
            this::class.java.getResourceAsStream(path)?.bufferedReader()?.readText()!!

        fun jupiterUrl(sgldis: String) = "/jupiterweb/obterTurma?sgldis=${sgldis}"

        fun janusDiscipline(sgldis: String) = "/janus/DisciplinaAux?tipo=T&sgldis=${sgldis}"

        fun janusOffering(sgldis: String, classCode: String = "2") =
            "/janus/TurmaDet?sgldis=${sgldis}&ofe=${classCode}"
    }

    private val mockEngine: HttpClientEngine by lazy {
        MockEngine { request ->
            respond(
                content =
                    when (request.url.fullPath) {
                        jupiterUrl("1") ->
                            ByteReadChannel(
                                getResource(
                                    "/sheets/DisciplineOffering/jupiter/multipleOfferings.html",
                                ),
                            )
                        jupiterUrl("2") ->
                            ByteReadChannel(
                                getResource(
                                    "/sheets/DisciplineOffering/jupiter/singleOffering.html",
                                ),
                            )
                        jupiterUrl("3") ->
                            ByteReadChannel(
                                getResource(
                                    "/sheets/DisciplineOffering/jupiter/alternativeLayout.html",
                                ),
                            )
                        janusDiscipline("") ->
                            ByteReadChannel(
                                getResource(
                                    "/sheets/DisciplineOffering/janus/Discipline.html",
                                ),
                            )
                        janusOffering("") ->
                            ByteReadChannel(
                                getResource(
                                    "/sheets/DisciplineOffering/janus/Offering.html",
                                ),
                            )
                        else -> {
                            println("unhandled url: ${request.url.fullPath}")
                            ByteReadChannel("")
                        }
                    },
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain"),
            )
        }
    }

    @Test
    fun `it can parse multiple offerings from Jupiter`() {
        val offerings = DisciplineOffering.trySetFromJupiter("1", 10000, mockEngine)

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
        val offerings = DisciplineOffering.trySetFromJupiter("2", 10000, mockEngine)

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
    fun `it can parse an alternative layout from Jupiter`() {
        val offerings = DisciplineOffering.trySetFromJupiter("3", 10000, mockEngine)

        assertEquals(
            setOf(
                DisciplineOffering(
                    classCode = "2025201",
                    startDate = "04/08/2025",
                    endDate = "12/12/2025",
                    professors = setOf("Professor 1", "Professor 2", "Professor 3"),
                ),
            ),
            offerings,
        )
    }

    @Test
    fun `it can parse a single offering from Janus`() {
        val offerings = DisciplineOffering.trySetFromJanus("", 10000, mockEngine)

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
