package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.server.app.configureSerialization
import br.usp.inovacao.hubusp.server.catalog.*
import br.usp.inovacao.hubusp.sheets.SpreadsheetWriter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class ConfigureCompanyRouteTest {
    @MockK private lateinit var mockMailer: Mailer
    @MockK private lateinit var mockSpreadsheetWriter: SpreadsheetWriter

    @Serializable
    data class RecvMessage(
        val errors: ValidationErrors,
    )

    @Serializable
    data class ValidationErrors(
        val company_data: Set<String> = emptySet(),
        val investment: Set<String> = emptySet(),
        val revenue: Set<String> = emptySet(),
        val incubation: Set<String> = emptySet(),
        val dna_usp_stamp: Set<String> = emptySet(),
        val staff: Set<String> = emptySet(),
        val partner: Set<String> = emptySet(),
        val about_company: Set<String> = emptySet(),
    )

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test POST valid company`() = testCompanyApplication {
        val response =
            client.post("/company") {
                contentType(ContentType.Application.Json)
                setBody(getResourceAsString("/routing/validCompanyForm.json"))
            }

        assertEquals(HttpStatusCode.Created, response.status)

        verify(exactly = 1) { mockMailer.send(any()) }
        verify(exactly = 1) { mockSpreadsheetWriter.append(any()) }
    }

    @Test
    fun `test POST invalid company`() = testCompanyApplication {
        val response =
            client.post("/company") {
                contentType(ContentType.Application.Json)
                setBody(getResourceAsString("/routing/invalidCompanyForm.json"))
            }

        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)

        verify(exactly = 0) { mockMailer.send(any()) }
        verify(exactly = 0) { mockSpreadsheetWriter.append(any()) }

        val recvMessage = response.bodyAsText().let { Json.decodeFromString<RecvMessage>(it) }
        val expectedRecvMessage =
            getResourceAsString("/routing/invalidCompanyFormErrors.json").let {
                Json.decodeFromString<RecvMessage>(it)
            }

        assertEquals(expectedRecvMessage, recvMessage)
    }

    private fun getResourceAsString(path: String) =
        this::class.java.getResourceAsStream(path)?.bufferedReader()?.readText()!!

    private fun testCompanyApplication(block: suspend ApplicationTestBuilder.() -> Unit) {
        every { mockMailer.send(any()) } returns Unit
        every { mockSpreadsheetWriter.append(any()) } returns ""

        testApplication {
            environment {
                config =
                    MapApplicationConfig(
                        "ktor.environment" to "dev",
                    )
            }

            application {
                configureCompanyRoute(
                    mockMailer,
                    emptyList(),
                    mockSpreadsheetWriter,
                )
                configureSerialization()
            }

            block()
        }
    }
}
