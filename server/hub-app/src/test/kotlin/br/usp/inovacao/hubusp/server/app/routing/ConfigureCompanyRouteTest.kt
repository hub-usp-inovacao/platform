package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.curatorship.register.step.Step
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.server.app.configureSerialization
import br.usp.inovacao.hubusp.sheets.SpreadsheetWriter
import io.ktor.client.request.forms.FormPart
import io.ktor.client.request.forms.InputProvider
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.config.MapApplicationConfig
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import io.ktor.utils.io.streams.asInput
import io.ktor.utils.io.streams.inputStream
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlin.io.path.createTempFile
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
        val errors: HashMap<Step, Set<String>>,
    )

    @BeforeTest
    fun setup() {
        MockKAnnotations.init(this)
    }

    @Test
    fun `test POST valid company`() = testCompanyApplication {
        val response =
            client.post("/company") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                FormPart(
                                    "company",
                                    getResourceAsString("/routing/validCompanyForm.json"),
                                ),
                            )
                        },
                    ),
                )
            }

        assertEquals(HttpStatusCode.Created, response.status)

        verify(exactly = 1) { mockMailer.send(withArg { assertEquals(2, it.attachments.size) }) }
        verify(exactly = 1) { mockSpreadsheetWriter.append(any()) }
    }

    @Test
    fun `test POST company with logo`() = testCompanyApplication {
        val response =
            client.post("/company") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                "logo",
                                InputProvider {
                                    this::class
                                        .java
                                        .getResourceAsStream("/routing/logo.webp")
                                        ?.asInput()!!
                                },
                                Headers.build {
                                    append(HttpHeaders.ContentType, "image/webp")
                                    append(HttpHeaders.ContentDisposition, "filename=\"logo.webp\"")
                                },
                            )
                            append(
                                FormPart(
                                    "company",
                                    getResourceAsString("/routing/validCompanyForm.json"),
                                ),
                            )
                        },
                    ),
                )
            }

        assertEquals(HttpStatusCode.Created, response.status)

        verify(exactly = 1) { mockMailer.send(withArg { assertEquals(3, it.attachments.size) }) }
        verify(exactly = 1) { mockSpreadsheetWriter.append(any()) }
    }

    @Test
    fun `test POST company with invalid logo`() = testCompanyApplication {
        val response =
            client.post("/company") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                "logo",
                                InputProvider { createTempFile().toFile().inputStream().asInput() },
                                Headers.build {
                                    append(HttpHeaders.ContentType, "image/webp")
                                    append(HttpHeaders.ContentDisposition, "filename=\"logo.webp\"")
                                },
                            )
                            append(
                                FormPart(
                                    "company",
                                    getResourceAsString("/routing/validCompanyForm.json"),
                                ),
                            )
                        },
                    ),
                )
            }

        assertEquals(HttpStatusCode.UnprocessableEntity, response.status)

        verify(exactly = 0) { mockMailer.send(any()) }
        verify(exactly = 0) { mockSpreadsheetWriter.append(any()) }

        val recvMessage = response.bodyAsText().let { Json.decodeFromString<RecvMessage>(it) }

        assert(recvMessage.errors.get(Step.CompanyData)?.firstOrNull()?.contains("logo") == true)
    }

    @Test
    fun `test POST invalid company`() = testCompanyApplication {
        val response =
            client.post("/company") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                FormPart(
                                    "company",
                                    getResourceAsString("/routing/invalidCompanyForm.json"),
                                ),
                            )
                        },
                    ),
                )
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
