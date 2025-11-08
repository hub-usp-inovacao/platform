package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.curatorship.companyform.CompanyForm
import br.usp.inovacao.hubusp.curatorship.companyform.CompanyFormValidationException
import br.usp.inovacao.hubusp.curatorship.companyform.ErrorsPerStep
import br.usp.inovacao.hubusp.curatorship.companyform.step.Step
import br.usp.inovacao.hubusp.mailer.Mail
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.server.app.auth.CompanyJWT
import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import br.usp.inovacao.hubusp.server.catalog.SearchCompanies
import br.usp.inovacao.hubusp.sheets.SpreadsheetWriter
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File
import kotlin.io.path.createTempFile
import kotlin.io.path.writeLines
import kotlin.io.path.writeText
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class) // explicitNulls
fun Application.configureCompanyRoute(
    mailer: Mailer,
    searchCompanies: SearchCompanies,
    companyRegisterFormSheet: SpreadsheetWriter,
    companyUpdateFormSheet: SpreadsheetWriter,
) {
    suspend fun panic(call: ApplicationCall, e: Exception) {
        // If this gets executed, then an exception somewhere was not caught. Fix it.

        call.respond(HttpStatusCode.InternalServerError)

        val subject = "[INTERNAL SERVER ERROR] ${call.request.httpMethod} ${call.request.uri}"

        this.log.warn("${subject}: ${e.stackTraceToString()}")

        mailer.send(
            Mail(
                to = Configuration.email.devs,
                subject,
                body = e.stackTraceToString(),
            ),
        )
    }

    suspend fun recvCompanyForm(call: ApplicationCall): Triple<String, CompanyForm, File?> {
        val multipartData = call.receiveMultipart()

        var companyFormJson: String? = null
        var companyForm: CompanyForm? = null
        var logo: File? = null

        multipartData.forEachPart { part ->
            when (part) {
                is PartData.FormItem -> {
                    when (part.name) {
                        "company" -> {
                            val json = Json { explicitNulls = false }
                            companyFormJson = part.value
                            companyForm = json.decodeFromString<CompanyForm>(part.value)
                        }
                    }
                }

                is PartData.FileItem -> {
                    val file = createTempFile().toFile()
                    part.provider().copyAndClose(file.writeChannel())
                    logo = file
                }

                else -> {}
            }
            part.dispose()
        }

        return Triple(companyFormJson!!, companyForm!!, logo)
    }

    suspend fun createCompanyFormAttachments(json: String, form: CompanyForm, logo: File?) =
        listOf(
            Mail.Attachment(
                "company.json",
                createTempFile().apply { writeText(json, Charsets.UTF_8) }.toFile(),
            ),
            Mail.Attachment(
                "company.csv",
                createTempFile()
                    .apply {
                        writeLines(
                            listOf(
                                CSV_HEADERS.joinToString(","),
                                form.toCsvRow().joinToString(","),
                            ),
                            Charsets.UTF_8,
                        )
                    }
                    .toFile(),
            ),
        ) +
            if (logo != null) {
                val image = Image(logo)
                listOf(
                    Mail.Attachment(
                        "logo.${image.extension}",
                        image.file,
                    ),
                )
            } else emptyList()

    suspend fun handleErrors(call: ApplicationCall, block: suspend () -> Unit) {
        @Serializable data class ErrorMessage(val errors: ErrorsPerStep)

        try {
            block()
        } catch (e: CompanyFormValidationException) {
            call.respond(
                HttpStatusCode.UnprocessableEntity,
                ErrorMessage(errors = e.errorsPerStep),
            )
        } catch (e: ImageValidationException) {
            call.respond(
                HttpStatusCode.UnprocessableEntity,
                ErrorMessage(
                    errors =
                        mapOf(
                            Step.CompanyData to setOf("logo: Imagem inválida. (${e.message})"),
                        ),
                ),
            )
        } catch (e: NotFoundException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "")
        } catch (e: BadRequestException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "")
        } catch (e: Exception) {
            panic(call, e)
        }
    }

    routing {
        post("/company/jwt") {
            handleErrors(call) {
                var recv: CompanyJWT

                try {
                    recv = call.receive<CompanyJWT>()
                } catch (e: Exception) {
                    throw BadRequestException(e.message)
                }

                var company: Company

                try {
                    company =
                        searchCompanies
                            .search(CompanySearchParams(cnpj = recv.cnpj))
                            .firstOrNull()!!
                } catch (_: Exception) {
                    throw NotFoundException("CNPJ not found")
                }

                val token = recv.createToken()

                mailer.send(
                    Mail(
                        to = company.emails,
                        subject = "Token de segurança para atualização de dados da empresa",
                        body =
                            """
Mensagem automática.

Foi solicitada uma atualização de dados da sua empresa na plataforma.

Para garantir a segurança desta solicitação, informe o token quando solicitado no site:

${token}

Ou acesse diretamente o link:

${Configuration.jwt.audience}/empresas/atualizar?token=${token}

Se não foi você que solicitou a atualização de dados, ignore este e-mail.
                        """,
                    ),
                )

                log.info("Company with cnpj ${recv.cnpj} requested JWT:\n${token}")

                call.respond(HttpStatusCode.OK)
            }
        }
        authenticate(CompanyJWT.providerName) {
            get("/company") {
                handleErrors(call) {
                    var principal: JWTPrincipal

                    try {
                        principal = call.principal<JWTPrincipal>()!!
                    } catch (e: Exception) {
                        throw BadRequestException(e.message)
                    }

                    val claim = CompanyJWT.fromPayload(principal.payload)
                    log.debug("GET /company: ${claim?.cnpj}")

                    try {
                        val company =
                            searchCompanies
                                .search(CompanySearchParams(cnpj = claim?.cnpj))
                                .firstOrNull()!!
                        call.respond(HttpStatusCode.OK, company)
                    } catch (e: Exception) {
                        throw NotFoundException("company not found")
                    }
                }
            }
            patch("/company") {
                handleErrors(call) {
                    var jwt: CompanyJWT

                    try {
                        jwt = CompanyJWT.fromPayload(call.principal<JWTPrincipal>()!!.payload)!!
                    } catch (e: Exception) {
                        throw BadRequestException(e.message)
                    }

                    val (companyFormJson, companyForm, logo) = recvCompanyForm(call)

                    if (companyForm.data.cnpj != jwt.cnpj) {
                        throw BadRequestException("company form cnpj does not match token")
                    }

                    log.debug("PATCH /company: ${jwt.cnpj}")

                    mailer.send(
                        Mail(
                            to = Configuration.email.devs,
                            cc = Configuration.email.cc,
                            subject = "Atualização de companhia",
                            body =
                                "Mensagem automática. Pedido de atualização de companhia recebida.",
                            attachments =
                                createCompanyFormAttachments(companyFormJson, companyForm, logo),
                        ),
                    )

                    companyUpdateFormSheet.append(listOf(companyForm.toCsvRow()))

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
        post("/company") {
            handleErrors(call) {
                val (companyFormJson, companyForm, logo) = recvCompanyForm(call)

                mailer.send(
                    Mail(
                        to = Configuration.email.devs,
                        cc = Configuration.email.cc,
                        subject = "Cadastro de companhia",
                        body = "Mensagem automática. Novo cadastro de companhia recebido.",
                        attachments =
                            createCompanyFormAttachments(companyFormJson, companyForm, logo),
                    ),
                )

                companyRegisterFormSheet.append(listOf(companyForm.toCsvRow()))

                call.respond(HttpStatusCode.Created)
            }
        }
    }
}

fun CompanyForm.toCsvRow(): List<String> {
    val row = mutableListOf(Time.timestamp())

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

    return row.map { (it ?: "").replace(",", ";") }
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
