package br.usp.inovacao.hubusp.server.app.modules

import br.usp.inovacao.hubusp.mailer.Mail
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.server.app.models.CompanyForm
import br.usp.inovacao.hubusp.server.catalog.*
import br.usp.inovacao.hubusp.server.persistence.*
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun String.toBooleanPTBR(): Boolean {
    val decoded = URLDecoder.decode(this, StandardCharsets.UTF_8.toString())
    return when (decoded) {
        "sim", "Sim", "SIM", "s", "S" -> true
        "não", "Não", "NÃO", "n", "N", "ñ", "Ñ" -> false
        else -> throw IllegalArgumentException("Invalid boolean value in PT-BR: $decoded (decoded from $this)")
    }
}

fun Parameters.toPDISearchParams() = PDISearchParams(
    categories = this["categories"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    term = this["term"],
)

fun Parameters.toResearcherSearchParams() = ResearcherSearchParams(
    majorArea = this["areaMajors"]?.split(",")?.toSet() ?: emptySet(),
    minorArea = this["areaMinors"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    unity = this["unity"],
    bond = this["bond"],
    term = this["term"]
)

fun Parameters.toCompanySearchParams() = CompanySearchParams(
    areaMajors = this["areaMajors"]?.split(',')?.toSet() ?: emptySet(),
    areaMinors = this["areaMinors"]?.split(',')?.toSet() ?: emptySet(),
    unity = this["unity"],
    city = this["city"],
    ecosystem = this["ecosystem"],
    size = this["size"],
    term = this["term"]
)

fun Parameters.toPatentSearchParams() = PatentSearchParams(
    majorAreas = this["areaMajors"]?.split(",")?.toSet() ?: emptySet(),
    minorAreas = this["areaMinors"]?.split(",")?.toSet() ?: emptySet(),
    status = this["status"],
    term = this["term"],
)

fun Parameters.toInitiativeSearchParams() = InitiativeSearchParams(
    classifications = this["classifications"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    term = this["term"]
)

fun Parameters.toDisciplineSearchParams() = DisciplineSearchParams(
    categories = this["categories"]?.split(",")?.toSet() ?: emptySet(),
    campus = this["campus"],
    unity = this["unity"],
    level = this["level"],
    nature = this["nature"],
    offeringPeriod = this["offeringPeriod"],
    beingOffered = this["beingOffered"]?.toBooleanPTBR(),
    term = this["term"],
)


fun Application.catalog(db: MongoDatabase) {
    val searchDisciplines = CatalogDisciplineRepositoryImpl(db)
        .let { SearchDisciplines(it) }

    val searchResearchers = CatalogResearcherRepositoryImpl(db)
        .let { SearchResearchers(it) }

    val searchCompanies = CatalogCompanyRepositoryImpl(db)
        .let { SearchCompanies(it) }

    val searchPatents = CatalogPatentRepositoryImpl(db)
        .let { SearchPatents(it) }

    val searchInitiatives = CatalogInitiativeRepositoryImpl(db)
        .let { SearchInitiatives(it) }

    val repo = CatalogPDIRepositoryImpl(db)
    val searchPDIs = SearchPDIs(repo)

    routing {
        get("/ecosystems") {
            val ecosystems = searchCompanies.getAllEcosystems()

            call.respond(
                HttpStatusCode.OK,
                mapOf("ecosystems" to ecosystems)
            )
        }

        get("/company_cities") {
            val cities = searchCompanies.getAllCities()

            call.respond(
                HttpStatusCode.OK,
                mapOf("cities" to cities)
            )
        }

        get("/patent_classifications") {
            val classifications = searchPatents.getAllClassifications()

            call.respond(
                HttpStatusCode.OK,
                mapOf("classifications" to classifications)
            )
        }

        get("/pdis") {
            val params = call.request.queryParameters

            val pdis = searchPDIs.search(params.toPDISearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("pdis" to pdis)
            )
        }

        get("/disciplines") {
            val params = call.request.queryParameters.toDisciplineSearchParams()
            val disciplines = searchDisciplines.search(params)

            call.respond(
                HttpStatusCode.OK,
                mapOf("disciplines" to disciplines)
            )
        }

        get("/skills") {
            val params = call.request.queryParameters

            val researchers = searchResearchers.search(params.toResearcherSearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("skills" to researchers)
            )
        }

        get("/companies") {
            val params = call.request.queryParameters

            val companies = searchCompanies.search(params.toCompanySearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("companies" to companies)
            )
        }

        get("/teste") {
            val params = call.request.queryParameters

            val companies = searchCompanies.search(params.toCompanySearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("companies" to companies)
            )
        }

        get("/patents") {
            val params = call.request.queryParameters

            val patents = searchPatents.search(params.toPatentSearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("patents" to patents)
            )
        }

        get("/initiatives") {
            val params = call.request.queryParameters

            val initiatives = searchInitiatives.search(params.toInitiativeSearchParams())

            call.respond(
                HttpStatusCode.OK,
                mapOf("initiatives" to initiatives)
            )
        }

        post("/company/register") {
            try {
                val contentType = call.request.headers["Content-Type"]
                val companyForm: CompanyForm
                var logoFile: PartData.FileItem? = null

                if (contentType?.startsWith("multipart/") == true) {
                    val multipart = call.receiveMultipart()
                    var companyFormTemp: CompanyForm? = null

                    multipart.forEachPart { part ->
                        when (part) {
                            is PartData.FormItem -> {
                                if (part.name == "companyData") {
                                    try {
                                        companyFormTemp = Json.decodeFromString(CompanyForm.serializer(), part.value)
                                    } catch (e: Exception) {
                                        println("Erro ao fazer parse do JSON: ${e.message}")
                                        println("JSON recebido: ${part.value}")
                                        throw IllegalArgumentException("Erro no formato do JSON: ${e.message}")
                                    }
                                }
                            }
                            is PartData.FileItem -> {
                                if (part.name == "logo") {
                                    logoFile = part
                                }
                            }
                            else -> {}
                        }
                        part.dispose()
                    }

                    companyForm = companyFormTemp ?: throw IllegalArgumentException("Dados da empresa não fornecidos")
                } else {
                    try {
                        companyForm = call.receive<CompanyForm>()
                        println("JSON recebido com sucesso: ${companyForm.name}")
                    } catch (e: Exception) {
                        println("Erro ao fazer parse do JSON direto: ${e.message}")
                        throw IllegalArgumentException("Erro no formato do JSON: ${e.message}")
                    }
                }

                val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")
                val timestamp = LocalDateTime.now().format(formatter)
                val csvFile = File.createTempFile("company_register_${timestamp}_", ".csv")

                csvFile.printWriter().use { out ->
                    val mainHeaders = listOf(
                        "nome", "razao_social", "cnpj", "ano_fundacao", "cnae",
                        "endereco", "bairro", "cidade", "estado", "cep",
                        "telefones", "emails", "descricao", "servicos",
                        "tecnologias", "site", "incubada", "ecossistemas",
                        "tamanho_empresa", "odss", "redes_sociais",
                        "quer_selo_dna", "email_contato_dna", "nome_contato_dna", "acordos_dna",
                        "categoria_dna", "status_confirmacao_dna", "funcionarios_clt",
                        "colaboradores_pj", "estagiarios_bolsistas", "total_funcionarios",
                        "tem_investimento", "tipos_investimento", "valor_proprio",
                        "valor_anjo", "valor_venture", "valor_private_equity",
                        "valor_pipe", "outros_investimentos", "faturamento",
                        "porte_rfb", "tipo_empresa", "status_operacional",
                        "total_socios", "tem_socios_usp", "nome_incubadora",
                        "eh_unicornio", "acordos_empresa", "status_confirmacao"
                    )

                    val partnerHeaders = companyForm.partners?.withIndex()?.flatMap { (index, _) ->
                        listOf(
                            "socio_${index + 1}_nome",
                            "socio_${index + 1}_nusp",
                            "socio_${index + 1}_vinculo",
                            "socio_${index + 1}_unidade",
                            "socio_${index + 1}_email",
                            "socio_${index + 1}_telefone",
                            "socio_${index + 1}_cargo"
                        )
                    } ?: emptyList()

                    val allHeaders = mainHeaders + partnerHeaders
                    out.println(allHeaders.joinToString(","))

                    val mainValues = listOf(
                        companyForm.name,
                        companyForm.corporateName,
                        companyForm.cnpj,
                        companyForm.year,
                        companyForm.cnae,
                        companyForm.address.venue,
                        companyForm.address.neighborhood,
                        companyForm.address.city,
                        companyForm.address.state,
                        companyForm.address.cep,
                        companyForm.phones.joinToString(";"),
                        companyForm.emails.joinToString(";"),
                        companyForm.description,
                        companyForm.services.joinToString(";"),
                        companyForm.technologies.joinToString(";"),
                        companyForm.url ?: "",
                        companyForm.incubated,
                        companyForm.ecosystems.joinToString(";"),
                        companyForm.companySize.joinToString(";"),
                        companyForm.odss?.joinToString(";") ?: "",
                        companyForm.socialMedias.joinToString(";"),
                        companyForm.dnaUspInfo.wantsSeal.toString(),
                        companyForm.dnaUspInfo.contactEmail ?: "",
                        companyForm.dnaUspInfo.contactName ?: "",
                        companyForm.dnaUspInfo.contactAgreements.joinToString(";"),
                        companyForm.dnaUspInfo.category ?: "",
                        companyForm.dnaUspInfo.confirmationStatus ?: "",
                        companyForm.employeeInfo.cltEmployees.toString(),
                        companyForm.employeeInfo.pjCollaborators.toString(),
                        companyForm.employeeInfo.internsScholars.toString(),
                        companyForm.employeeInfo.totalEmployees.toString(),
                        companyForm.investmentInfo.hasInvestment.toString(),
                        companyForm.investmentInfo.investmentTypes.joinToString(";"),
                        companyForm.investmentInfo.ownInvestmentAmount ?: "",
                        companyForm.investmentInfo.angelInvestmentAmount ?: "",
                        companyForm.investmentInfo.ventureCapitalAmount ?: "",
                        companyForm.investmentInfo.privateEquityAmount ?: "",
                        companyForm.investmentInfo.pipeAmount ?: "",
                        companyForm.investmentInfo.otherInvestmentsAmount ?: "",
                        companyForm.investmentInfo.revenue ?: "",
                        companyForm.investmentInfo.companySize ?: "",
                        companyForm.companyType ?: "",
                        companyForm.operationalStatus ?: "",
                        companyForm.totalPartners?.toString() ?: "",
                        companyForm.hasUspPartners?.toString() ?: "",
                        companyForm.incubatorName ?: "",
                        companyForm.isUnicorn?.toString() ?: "",
                        companyForm.agreements.joinToString(";"),
                        "Pendente"
                    ).map {
                        if (it.contains(",")) "\"${it}\"" else it
                    }

                    val partnerValues = companyForm.partners?.flatMap { partner ->
                        listOf(
                            partner.name,
                            partner.nusp ?: "",
                            partner.bond ?: "",
                            partner.unity ?: "",
                            partner.email ?: "",
                            partner.phone ?: "",
                            partner.position ?: ""
                        ).map {
                            if (it.contains(",")) "\"${it}\"" else it
                        }
                    } ?: emptyList()

                    val allValues = mainValues + partnerValues
                    out.println(allValues.joinToString(","))
                }

                val emailUser = System.getenv("HUB_EMAIL_ADDRESS")
                val emailPassword = System.getenv("HUB_EMAIL_PASSWORD")

                val mailer = Mailer(emailUser, emailPassword)
                val attachments = mutableListOf<Mail.Attachment>()

                attachments.add(Mail.Attachment("cadastro_empresa_${timestamp}.csv", csvFile))

                if (logoFile != null) {
                    val logoBytes = logoFile!!.streamProvider().readBytes()
                    val logoFileName = "logo_${companyForm.cnpj.replace(Regex("[^\\d]"), "")}.${logoFile!!.originalFileName?.substringAfterLast('.') ?: "jpg"}"
                    val logoTempFile = File.createTempFile("logo_temp", ".${logoFile!!.originalFileName?.substringAfterLast('.') ?: "jpg"}")
                    logoTempFile.writeBytes(logoBytes)
                    attachments.add(Mail.Attachment(logoFileName, logoTempFile))
                }

                val mail = Mail(
                    to = listOf("paraquedasshow@gmail.com"),
                    subject = "Cadastro de Empresa: ${companyForm.name}",
                    body = """
                        Segue em anexo os dados de cadastro da empresa ${companyForm.name}.
                        
                        Resumo do cadastro:
                        - Nome: ${companyForm.name}
                        - CNPJ: ${companyForm.cnpj}
                        - Ano de fundação: ${companyForm.year}
                        - Cidade: ${companyForm.address.city}
                        - Número de sócios: ${companyForm.partners?.size ?: 0}
                        - Possui sócios USP: ${companyForm.hasUspPartners ?: false}
                        - Quer selo DNA USP: ${companyForm.dnaUspInfo.wantsSeal}
                        - Funcionários CLT: ${companyForm.employeeInfo.cltEmployees}
                        - Colaboradores PJ: ${companyForm.employeeInfo.pjCollaborators}
                        - Estagiários/Bolsistas: ${companyForm.employeeInfo.internsScholars}
                        - Logo enviado: ${if (logoFile != null) "Sim" else "Não"}
                    """.trimIndent(),
                    attachments = attachments
                )

                mailer.send(mail)

                call.respond(HttpStatusCode.OK, mapOf("status" to "enviado"))

            } catch (e: IllegalArgumentException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    mapOf("status" to "erro", "mensagem" to e.message)
                )
            } catch (e: Exception) {
                println("Erro no endpoint /company/register: ${e.message}")
                e.printStackTrace()
                call.respond(
                    HttpStatusCode.InternalServerError,
                    mapOf("status" to "erro", "mensagem" to "Erro interno: ${e.message}")
                )
            }
        }
    }
}
