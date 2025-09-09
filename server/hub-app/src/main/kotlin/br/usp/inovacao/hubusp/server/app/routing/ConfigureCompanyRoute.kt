package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.curatorship.register.CompanyForm
import br.usp.inovacao.hubusp.curatorship.register.CompanyFormValidationException
import br.usp.inovacao.hubusp.mailer.Mail
import br.usp.inovacao.hubusp.mailer.Mailer
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.receiveText
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.nio.file.Files
import kotlin.io.path.writeText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class) // explicitNulls
fun Application.configureCompanyRoute(mailer: Mailer, recipientList: List<String>) {
    routing {
        post("/company") {
            @Serializable data class Message(val company: CompanyForm)

            try {
                val text = call.receiveText()

                val file =
                    // TODO: to CSV
                    Files.createTempFile("tmp-company", ".json").apply {
                        writeText(text, Charsets.UTF_8)
                    }

                val json = Json { explicitNulls = false }
                val message = json.decodeFromString<Message>(text)

                mailer.send(
                    Mail(
                        to = recipientList,
                        subject = "Cadastro de companhia",
                        body = message.toString(),
                        attachments = listOf(Mail.Attachment("company.json", file.toFile())),
                    ))

                call.respond(HttpStatusCode.Created)
            } catch (e: CompanyFormValidationException) {
                call.respond(HttpStatusCode.UnprocessableEntity, e.errorsPerStep)
            } catch (e: Exception) {
                application.log.warn(
                    """Internal Server Error
- path: ${call.request.uri}
- error: ${e.toString()}""",
                )

                call.respondText(
                    e.message ?: "", ContentType.Text.Plain, HttpStatusCode.InternalServerError)
            }
        }
    }
}
