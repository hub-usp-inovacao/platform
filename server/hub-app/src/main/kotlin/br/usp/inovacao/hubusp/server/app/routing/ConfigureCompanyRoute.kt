package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.curatorship.register.CompanyForm
import br.usp.inovacao.hubusp.curatorship.register.CompanyFormValidationException
import br.usp.inovacao.hubusp.curatorship.register.ErrorsPerStep
import br.usp.inovacao.hubusp.mailer.Mail
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.sheets.SpreadsheetWriter
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.writeLines
import kotlin.io.path.writeText
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class) // explicitNulls
fun Application.configureCompanyRoute(
    mailer: Mailer,
    recipientList: List<String>,
    spreadsheetWriter: SpreadsheetWriter
) {
    routing {
        post("/company") {
            @Serializable data class RecvMessage(val company: CompanyForm)
            @Serializable data class ErrorMessage(val errors: ErrorsPerStep)

            try {
                val text = call.receiveText()

                val json = Json { explicitNulls = false }
                val message = json.decodeFromString<RecvMessage>(text)
                val row = message.company.toCsvRow()

                mailer.send(
                    Mail(
                        to = recipientList,
                        subject = "Cadastro de companhia",
                        body = "Mensagem automática. Novo cadastro de companhia recebido.",
                        attachments =
                            listOf(
                                Mail.Attachment(
                                    "company.json",
                                    Files.createTempFile("company", ".json")
                                        .apply { writeText(text, Charsets.UTF_8) }
                                        .toFile(),
                                ),
                                Mail.Attachment(
                                    "company.csv",
                                    Files.createTempFile("company", ".csv")
                                        .apply {
                                            writeLines(
                                                listOf(
                                                    CSV_HEADERS.joinToString(","),
                                                    row.joinToString(","),
                                                ),
                                                Charsets.UTF_8,
                                            )
                                        }
                                        .toFile(),
                                ),
                            ),
                    ),
                )

                spreadsheetWriter.append(listOf(row))

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

fun CompanyForm.toCsvRow(): List<String> {
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
            this.about.logo.toCsvCell(),
            this.about.site.toCsvCell(),
            this.incubation.wasIncubated,
            this.incubation.ecosystem,
            "", // "Unicórnio"
            "", // "Número total de colaboradores"
            this.about.odss.toCsvCell(),
        )

    // {X..Z} AA
    // Social medias

    val linkedin = this.about.socialMedias.filter { it.contains("linkedin.com") }
    val instagram = this.about.socialMedias.filter { it.contains("instagram.com") }
    val youtube =
        this.about.socialMedias.filter { it.contains("youtube.com") || it.contains("youtu.be") }
    val facebook =
        this.about.socialMedias.filter {
            it.contains("facebook.com") || it.contains("fb.me") || it.contains("fb.com")
        }
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
                "", // "Por qual email podemos entrar em contato para tratar do uso da
                // marca..."
                "", // "Qual o nome do responsável por este email?"
            )
        }

    row +=
        listOf(
            "", // "Contrato para uso da marca DNA USP"
            this.dnaUspStamp.truthfulInformations.toCsvCell(),
            this.dnaUspStamp.permissions.toCsvCell(),
        )

    // A{H..N}
    // First partner

    val firstPartner = this.partners.getOrNull(0)

    row +=
        listOf(
            firstPartner?.name ?: "",
            firstPartner?.nusp ?: "",
            firstPartner?.bond ?: "",
            firstPartner?.unit ?: "",
            firstPartner?.role ?: "",
            firstPartner?.email ?: "",
            firstPartner?.phone ?: "",
        )

    // A{O,P}
    // Number of partners

    row +=
        listOf(
            this.partners.size.toString(),
            "", // "Possui sócios que mantiveram ou mantêm vínculo com a USP"
        )

    // A{Q..Z} B{A..J}
    // Remaining 4 partners

    for (i in 1..4) {
        val partner = this.partners.getOrNull(i)

        row +=
            listOf(
                "", // "Gostaria de adicionar os dados dos demais sócios"
                partner?.name ?: "",
                partner?.nusp ?: "",
                partner?.bond ?: "",
                partner?.unit ?: "",
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
            "", // "Somatório (sócios + CLT + PJ + E/B)"
            this.data.companyNature,
            this.data.registryStatus,
            "", // "Índice"
            "", // "Vínculo com a incubadora"
            "", // "Confirmação de vínculo"
            this.data.category,
            "", // "Confirmação de vínculo EMPRESA"
        )

    return row.map { it.replace(",", ";") }
}

fun String?.toCsvCell() = this ?: "N/D"

fun Set<String>.toCsvCell() = if (this.isEmpty()) "N/D" else this.joinToString(";")

fun List<String>.toCsvCell() = if (this.isEmpty()) "N/D" else this.joinToString(";")

fun Boolean?.toCsvCell() = if (this == null) "N/D" else if (this) "Sim" else "Não"

val CSV_HEADERS =
    listOf(
        "data e hora da requisição de cadastro",
        "CNPJ",
        "Nome fantasia da empresa",
        "Razão social da empresa",
        "Ano de fundação",
        "CNAE (Classificação Nacional de Atividades Econômicas) da empresa",
        "Telefone comercial",
        "Email institucional",
        "Endereço da empresa",
        "Bairro",
        "Cidade",
        "Estado",
        "CEP",
        "Insira uma breve descrição da empresa",
        "Liste os principais produtos e/ou serviços oferecidos",
        "Caso a sua empresa atue no desenvolvimento de tecnologias",
        "Logotipo da empresa",
        "Site da empresa",
        "A empresa está ou esteve em alguma incubadora ou Parque tecnológico?",
        "Em qual incubadora ou Parque Tecnológico?",
        "Unicórnio?",
        "Número total de colaboradores",
        "ODS",
        "LinkedIn",
        "Instagram",
        "Youtube",
        "Facebook",
        "Sua empresa gostaria de receber o selo DNA USP?",
        "Por qual email podemos entrar em contato para tratar do uso da marca DNA USP?",
        "Qual o nome do responsável por este email?",
        "Contrato para uso da marca DNA USP (enviado ou assinado)",
        "Declaro que as informações fornecidas são verdadeiras e que a empresa atende aos critérios estabelecidos",
        "Selecione as opções com as quais a empresa está de acordo",
        "Nome completo do empreendedor que possuiu ou mantém vínculo com a USP (sem abreviações)",
        "Número USP (Sócio 1)",
        "Qual tipo de vínculo já possuiu ou ainda mantém com a USP?",
        "Com qual instituto;escola ou centro é o vínculo atual ou mais recente?",
        "Cargo",
        "Email do empreendedor",
        "Telefone (fixo ou celular)",
        "Quantos sócios a empresa possui?",
        "Possui sócios que mantiveram ou mantêm vínculo com a USP?",
        "Gostaria de adicionar os dados dos demais sócios?",
        "Nome completo (Sócio 2)",
        "Número USP (Sócio 2)",
        "Qual o tipo de vínculo possuiu ou mantém com a USP (Sócio 2)?",
        "Com qual instituto;escola ou centro é o vínculo atual ou mais recente?",
        "Deseja adicionar os dados de outro sócio?",
        "Nome completo (Sócio 3)",
        "Número USP (Sócio 3)",
        "Qual o tipo de vínculo possuiu ou mantém com a USP (Sócio 3)?",
        "Com qual instituto;escola ou centro é o vínculo atual ou mais recente?",
        "Deseja adicionar os dados de outro sócio?",
        "Nome completo (Sócio 4)",
        "Número USP (Sócio 4)",
        "Qual o tipo de vínculo possuiu ou mantém com a USP (Sócio 4)?",
        "Com qual instituto;escola ou centro é o vínculo atual ou mais recente?",
        "Deseja adicionar os dados de outro sócio?",
        "Nome completo (Sócio 5)",
        "Número USP (Sócio 5)",
        "Qual o tipo de vínculo possuiu ou mantém com a USP (Sócio 5)?",
        "Com qual instituto;escola ou centro é o vínculo atual ou mais recente?",
        "Qual o número de funcionários contratados como CLT?",
        "Qual o número de colaboradores contratados como pessoa jurídica (PJ)?",
        "Qual o número de estagiários/bolsistas contratados?",
        "A empresa recebeu investimento?",
        "Qual(is) investimento(s) a empresa recebeu?",
        "Valor do investimento próprio (R$)",
        "Valor do investimento-anjo (R$)",
        "Valor do Venture Capital (R$)",
        "Valor do Private Equity (R$)",
        "Valor do PIPE-FAPESP (R$)",
        "Outros investimentos (R$)",
        "Qual foi o faturamento da empresa em 2022? (R$)",
        "Porte (RFB)",
        "Somatório (sócios + CLT + PJ + E/B)",
        "Tipo de empresa",
        "Status operacional da empresa",
        "Índice",
        "Vínculo com a incubadora",
        "Confirmação de vínculo USP",
        "Indique em qual categoria DNA USP sua empresa se encontra:",
        "Confirmação de vínculo EMPRESA",
    )
