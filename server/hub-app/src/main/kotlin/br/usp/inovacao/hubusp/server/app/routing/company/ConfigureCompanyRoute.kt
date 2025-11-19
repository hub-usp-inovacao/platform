package br.usp.inovacao.hubusp.server.app.routing.company

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.mailer.Mail
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.server.app.auth.CompanyJWT
import br.usp.inovacao.hubusp.server.app.routing.RoutingException
import br.usp.inovacao.hubusp.server.catalog.Company
import br.usp.inovacao.hubusp.server.catalog.CompanySearchParams
import br.usp.inovacao.hubusp.server.catalog.SearchCompanies
import br.usp.inovacao.hubusp.sheets.SpreadsheetWriter
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.httpMethod
import io.ktor.server.request.receive
import io.ktor.server.request.uri
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.coroutines.*
import kotlinx.serialization.ExperimentalSerializationApi

@OptIn(ExperimentalSerializationApi::class) // explicitNulls
fun Application.configureCompanyRoute(
    mailer: Mailer,
    searchCompanies: SearchCompanies,
    companyRegisterFormSheet: SpreadsheetWriter,
    companyUpdateFormSheet: SpreadsheetWriter,
) {
    suspend fun handleErrors(call: ApplicationCall, block: suspend () -> Unit) {
        try {
            block()
        } catch (e: RoutingException.NotFoundException) {
            call.respond(HttpStatusCode.NotFound, e.message ?: "")
        } catch (e: RoutingException.BadRequestException) {
            call.respond(HttpStatusCode.BadRequest, e.message ?: "")
        } catch (e: Exception) {
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
    }

    routing {
        post("/company/jwt") {
            handleErrors(call) {
                var recv: CompanyJWT

                try {
                    recv = call.receive<CompanyJWT>()
                } catch (e: Exception) {
                    throw RoutingException.BadRequestException(
                        "Failed to receive company jwt: ${e.message}")
                }

                var company: Company

                try {
                    company =
                        searchCompanies
                            .search(CompanySearchParams(cnpj = recv.cnpj))
                            .firstOrNull()!!
                } catch (_: Exception) {
                    throw RoutingException.NotFoundException("CNPJ not found")
                }

                val token = recv.createToken()

                mailer.send(
                    Mail(
                        to = company.emails,
                        subject = "Token de segurança para atualização de dados da empresa DNA USP",
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
                    var jwt: CompanyJWT

                    try {
                        jwt = CompanyJWT.fromPayload(call.principal<JWTPrincipal>()!!.payload)!!
                    } catch (e: Exception) {
                        throw RoutingException.BadRequestException(
                            "Failed to parse company jwt: ${e.message}")
                    }

                    try {
                        val company =
                            searchCompanies
                                .search(CompanySearchParams(cnpj = jwt.cnpj))
                                .firstOrNull()!!

                        call.respond(HttpStatusCode.OK, company)
                    } catch (e: Exception) {
                        throw RoutingException.NotFoundException("CNPJ not found")
                    }
                }
            }
            patch("/company") {
                handleErrors(call) {
                    var jwt: CompanyJWT

                    try {
                        jwt = CompanyJWT.fromPayload(call.principal<JWTPrincipal>()!!.payload)!!
                    } catch (e: Exception) {
                        throw RoutingException.BadRequestException(
                            "Failed to parse company jwt: ${e.message}",
                        )
                    }

                    val companyFormRequest = CompanyFormRequest.recv(call)

                    if (companyFormRequest.form.data.cnpj != jwt.cnpj) {
                        throw RoutingException.BadRequestException(
                            "company form cnpj does not match token")
                    }

                    log.debug("PATCH /company: ${jwt.cnpj}")

                    mailer.send(
                        Mail(
                            to = Configuration.email.devs,
                            cc = Configuration.email.cc,
                            subject = "Atualização de empresa DNA USP",
                            body =
                                "Mensagem automática. Pedido de atualização de empresa DNA USP recebida.",
                            attachments = companyFormRequest.createMailAttachments(),
                        ),
                    )

                    companyUpdateFormSheet.append(companyFormRequest.createCsvTable())

                    call.respond(HttpStatusCode.OK)
                }
            }
        }
        post("/company") {
            handleErrors(call) {
                val companyFormRequest = CompanyFormRequest.recv(call)

                mailer.send(
                    Mail(
                        to = Configuration.email.devs,
                        cc = Configuration.email.cc,
                        subject = "Cadastro de empresa DNA USP",
                        body = "Mensagem automática. Novo cadastro de empresa DNA USP recebido.",
                        attachments = companyFormRequest.createMailAttachments(),
                    ),
                )

                companyRegisterFormSheet.append(companyFormRequest.createCsvTable())

                call.respond(HttpStatusCode.Created)
            }
        }
    }
}
