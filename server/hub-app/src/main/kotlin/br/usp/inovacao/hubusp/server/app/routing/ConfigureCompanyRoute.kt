package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.mailer.Mail
import br.usp.inovacao.hubusp.mailer.Mailer
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receiveText
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.nio.file.Files
import kotlin.io.path.writeText

fun Application.configureCompanyRoute(mailer: Mailer, recipientList: List<String>) {
    routing {
        post("/company") {
            // TODO: Deserialize JSON -> CompanyStep
            // TODO: Validation, return constraint validation errors
            val text = call.receiveText()

            val file =
                Files.createTempFile("tmp-company", ".json").apply {
                    writeText(text, Charsets.UTF_8)
                }

            mailer.send(
                Mail(
                    to = recipientList,
                    subject = "Cadastro de companhia",
                    body = "", // TODO: Make it optional
                    attachments = listOf(Mail.Attachment("company.json", file.toFile())),
                ))

            call.respond(HttpStatusCode.OK)
        }
    }
}
