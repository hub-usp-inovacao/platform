package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.curatorship.register.CompanyForm
import br.usp.inovacao.hubusp.curatorship.register.CompanyFormValidationException
import br.usp.inovacao.hubusp.curatorship.register.ErrorsPerStep
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
            @Serializable data class RecvMessage(val company: CompanyForm)
            @Serializable data class ErrorMessage(val errors: ErrorsPerStep)

            try {
                val text = call.receiveText()

                val file =
                    // TODO: to CSV
                    Files.createTempFile("tmp-company", ".json").apply {
                        writeText(text, Charsets.UTF_8)
                    }

                val json = Json { explicitNulls = false }
                val message = json.decodeFromString<RecvMessage>(text)

                mailer.send(
                    Mail(
                        to = recipientList,
                        subject = "Cadastro de companhia",
                        body = message.toString(),
                        attachments = listOf(Mail.Attachment("company.json", file.toFile())),
                    ))

                call.respond(HttpStatusCode.Created)
            } catch (e: CompanyFormValidationException) {
                call.respond(
                    HttpStatusCode.UnprocessableEntity,
                    ErrorMessage(errors = e.errorsPerStep),
                )
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

fun CompanyForm.toCsvRow(): List<String?> {
    val row =
        mutableListOf(
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")),
        )

    // {A..M}

    row +=
        listOf(
            this.data.cnpj,
            this.data.publicName,
            this.data.corporateName,
            this.data.year,
            this.data.cnae,
            this.data.phones.toCsvCell(),
            this.data.emails.toCsvCell(),
            this.data.street,
            this.data.neighborhood,
            this.data.city,
            this.data.state,
            this.data.zipcode,
        )

    // {N..W}

    row +=
        listOf(
            this.about.description,
            this.about.services.toCsvCell(),
            this.about.technologies.toCsvCell(),
            this.about.logo,
            this.about.site,
            this.incubation.wasIncubated,
            this.incubation.ecosystem,
            null, // "Unicórnio"
            null, // "Número total de colaboradores"
            this.about.odss.toCsvCell(),
        )

    // {X..Z} AA
    // Social medias

    val linkedin = this.about.socialMedias.filter { it.contains("linkedin.com") }.toCsvCell()
    val instagram = this.about.socialMedias.filter { it.contains("instagram.com") }.toCsvCell()
    val youtube =
        this.about.socialMedias
            .filter { it.contains("youtube.com") || it.contains("youtu.be") }
            .toCsvCell()
    val facebook =
        this.about.socialMedias
            .filter { it.contains("facebook.com") || it.contains("fb.me") || it.contains("fb.com") }
            .toCsvCell()
    val otherSocialMedias = this.about.socialMedias - linkedin - instagram - youtube - facebook

    row +=
        listOf(
            linkedin.toCsvCell(),
            instagram.toCsvCell(),
            youtube.toCsvCell(),
            (facebook + otherSocialMedias).toCsvCell(),
        )

    // A{B..G}
    // DNA USP stamp

    row += this.dnaUspStamp.wantsStamp.toCsvCell()

    row +=
        if (this.dnaUspStamp.wantsStamp ?: false) {
            listOf(
                this.dnaUspStamp.email,
                this.dnaUspStamp.name,
            )
        } else {
            listOf(
                null, // "Por qual email podemos entrar em contato para tratar do uso da
                // marca..."
                null, // "Qual o nome do responsável por este email?"
            )
        }

    row +=
        listOf(
            null, // "Contrato para uso da marca DNA USP"
            this.dnaUspStamp.truthfulInformations.toCsvCell(),
            this.dnaUspStamp.permissions.toCsvCell(),
        )

    // A{H..N}
    // First partner

    val firstPartner = this.partners.getOrNull(0)

    row +=
        listOf(
            firstPartner?.name,
            firstPartner?.nusp,
            firstPartner?.bond,
            firstPartner?.unit,
            firstPartner?.role,
            firstPartner?.email,
            firstPartner?.phone,
        )

    // A{O,P}
    // Number of partners

    row +=
        listOf(
            this.partners.size.toString(),
            null, // "Possui sócios que mantiveram ou mantêm vínculo com a USP"
        )

    // A{Q..Z} B{A..J}
    // Remaining 4 partners

    for (i in 1..4) {
        val partner = this.partners.getOrNull(i)

        row +=
            listOf(
                null, // "Gostaria de adicionar os dados dos demais sócios"
                partner?.name,
                partner?.nusp,
                partner?.bond,
                partner?.unit,
            )
    }

    // B{K,L,M}

    row +=
        listOf(
            this.staff.numberOfCltEmployees,
            this.staff.numberOfPjColaborators,
            this.staff.numberOfInterns,
        )

    // B{N..U}

    row +=
        listOf(
            this.investment.received,
            this.investment.investmentsReceived.toCsvCell(),
            this.investment.own,
            this.investment.angel,
            this.investment.venture,
            this.investment.equity,
            this.investment.pipe,
            this.investment.others,
        )

    // BV
    row += this.revenue.lastYearRevenue

    // B{W..Z} C{A..E}
    row +=
        listOf(
            this.data.size,
            null, // "Somatório (sócios + CLT + PJ + E/B)"
            this.data.companyNature,
            this.data.registryStatus,
            null, // "Índice"
            null, // "Vínculo com a incubadora"
            null, // "Confirmação de vínculo"
            this.data.category,
            null, // "Confirmação de vínculo EMPRESA"
        )

    return row
}

fun String?.toCsvCell() = this ?: "N/D"

fun Set<String>.toCsvCell() = if (this.isEmpty()) "N/D" else this.joinToString(";")

fun List<String>.toCsvCell() = if (this.isEmpty()) "N/D" else this.joinToString(";")

fun Boolean?.toCsvCell() = if (this == null) "N/D" else if (this) "Sim" else "Não"
