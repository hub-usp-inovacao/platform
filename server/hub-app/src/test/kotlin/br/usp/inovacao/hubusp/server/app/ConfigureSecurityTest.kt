package br.usp.inovacao.hubusp.server.app

import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals

class ConfigureSecurityTest {
    @Test
    fun `it installs security`() =
        testApplication {
            application {
                configureSecurity()
                routing {
                    authenticate {
                        get("/admin-only") {
                            call.respondText("ok")
                        }
                    }
                }
            }

            val response = client.get("/admin-only")

            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
}
