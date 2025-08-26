package br.usp.inovacao.hubusp.server.app.services

import br.usp.inovacao.hubusp.server.app.modules.CompanyForm
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
private data class GoogleScriptRequest(
    val headers: List<String>,
    val rowData: List<String>
)

@Serializable
private data class GoogleScriptResponse(
    val status: String,
    val message: String,
    val row: Int? = null
)

class GoogleSheetsService {

    private val GOOGLE_APPS_SCRIPT_URL = "https://script.google.com/macros/s/AKfycbxrJQS5ZlqG2m0PFTdLbie6RQfw68H6rDJuwC8Mrh1wlj3aVIuNQoePhmzAbZ5WsugIjQ/exec"

    private val httpClient: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
        followRedirects = true
        engine {
            requestTimeout = 60000
        }
    }

    fun addCompanyToSheet(companyForm: CompanyForm) {
        try {
            val headers = createHeaders(companyForm)
            val companyData = createCompanyRow(companyForm)

            runBlocking {
                val success = addToSheetViaScript(headers, companyData)
                if (success) {
                    println("✅ Empresa ${companyForm.name} adicionada na planilha com sucesso!")
                } else {
                    throw RuntimeException("Falha ao adicionar dados na planilha")
                }
            }
        } catch (e: Exception) {
            println("❌ Erro ao processar empresa: ${e.message}")
            throw RuntimeException("Erro ao processar dados da empresa: ${e.message}", e)
        }
    }

    private suspend fun addToSheetViaScript(headers: List<String>, data: List<String>): Boolean {
        return try {
            val requestData = GoogleScriptRequest(
                headers = headers,
                rowData = data
            )

            val response = httpClient.post(GOOGLE_APPS_SCRIPT_URL) {
                contentType(ContentType.Application.Json)
                setBody(requestData)
                headers {
                    append("Accept", "application/json")
                    append("User-Agent", "HubUSP-CompanyRegistration/1.0")
                }
            }

            when (response.status) {
                HttpStatusCode.OK -> {
                    val responseBody = response.bodyAsText()
                    return try {
                        val scriptResponse = Json.decodeFromString(GoogleScriptResponse.serializer(), responseBody)
                        scriptResponse.status == "success"
                    } catch (_: Exception) {
                        responseBody.contains("success", ignoreCase = true) ||
                        responseBody.contains("adicionado", ignoreCase = true) ||
                        responseBody.contains("inserido", ignoreCase = true)
                    }
                }
                HttpStatusCode.Found, HttpStatusCode.MovedPermanently -> true
                else -> false
            }
        } catch (e: Exception) {
            println("❌ Erro na comunicação com Google Apps Script: ${e.message}")
            false
        }
    }

    private fun createHeaders(companyForm: CompanyForm): List<String> {
        return listOf(
            "cnpj", "nome_fantasia", "razao_social", "ano_fundacao", "cnae",
            "telefone_comercial", "email_institucional", "endereco", "bairro",
            "cidade", "estado", "cep", "descricao", "produtos_servicos",
            "tecnologias", "logotipo", "site", "incubadora_parque",
            "qual_incubadora", "unicornio", "total_colaboradores", "ods",
            "linkedin", "instagram", "youtube", "facebook", "quer_selo_dna",
            "email_contato_dna", "nome_responsavel_dna", "contrato_dna",
            "informacoes_verdadeiras", "acordos_empresa", "nomes_socios",
            "nusps_socios", "vinculos_socios", "unidades_socios", "cargos_socios",
            "emails_socios", "telefones_socios", "total_socios",
            "tem_socios_usp", "adicionar_mais_socios", "funcionarios_clt",
            "colaboradores_pj", "estagiarios_bolsistas", "recebeu_investimento",
            "tipos_investimento", "valor_proprio", "valor_anjo",
            "valor_venture_capital", "valor_private_equity", "valor_pipe",
            "outros_investimentos", "faturamento_2022", "porte_rfb",
            "somatorio_colaboradores", "tipo_empresa", "status_operacional",
            "indice", "vinculo_incubadora", "confirmacao_vinculo_usp",
            "categoria_dna_usp", "confirmacao_vinculo_empresa"
        )
    }

    private fun createCompanyRow(companyForm: CompanyForm): List<String> {
        val names: List<String>
        val nusps: List<String>
        val bonds: List<String>
        val unities: List<String>
        val positions: List<String>
        val emails: List<String>
        val phones: List<String>

        if (companyForm.partners != null && companyForm.partners.isNotEmpty()) {
            names = companyForm.partners.map { it.name }
            nusps = companyForm.partners.map { it.nusp ?: "" }
            bonds = companyForm.partners.map { it.bond ?: "" }
            unities = companyForm.partners.map { it.unity ?: "" }
            positions = companyForm.partners.map { it.position ?: "" }
            emails = companyForm.partners.map { it.email ?: "" }
            phones = companyForm.partners.map { it.phone ?: "" }
        } else {
            names = companyForm.partnerNames
            nusps = companyForm.partnerNusps
            bonds = companyForm.partnerBonds
            unities = companyForm.partnerUnities
            positions = companyForm.partnerPositions
            emails = companyForm.partnerEmails
            phones = companyForm.partnerPhones
        }

        return listOf(
            companyForm.cnpj,
            companyForm.name,
            companyForm.corporateName,
            companyForm.year,
            companyForm.cnae,
            companyForm.phones.joinToString(";"),
            companyForm.emails.joinToString(";"),
            companyForm.address.venue,
            companyForm.address.neighborhood,
            companyForm.address.city,
            companyForm.address.state,
            companyForm.address.cep,
            companyForm.description,
            companyForm.services.joinToString(";"),
            companyForm.technologies.joinToString(";"),
            companyForm.logo ?: "",
            companyForm.url ?: "",
            companyForm.incubated,
            companyForm.ecosystems.joinToString(";"),
            if (companyForm.isUnicorn == true) "Sim" else "Não",
            companyForm.totalCollaborators?.toString() ?: "",
            companyForm.odss?.joinToString(";") ?: "",
            companyForm.linkedin ?: "",
            companyForm.instagram ?: "",
            companyForm.youtube ?: "",
            companyForm.facebook ?: "",
            if (companyForm.dnaUspWanted) "Sim" else "Não",
            companyForm.dnaUspContactEmail ?: "",
            companyForm.dnaUspContactName ?: "",
            companyForm.dnaUspContract ?: "",
            if (companyForm.truthfulInformation) "Sim" else "Não",
            companyForm.agreementOptions.joinToString(";"),
            names.mapIndexed { index: Int, name: String -> "Sócio ${index + 1}: $name" }.joinToString(";"),
            nusps.mapIndexed { index: Int, nusp: String -> "NUSP Sócio ${index + 1}: $nusp" }.joinToString(";"),
            bonds.mapIndexed { index: Int, bond: String -> "Vínculo Sócio ${index + 1}: $bond" }.joinToString(";"),
            unities.mapIndexed { index: Int, unity: String -> "Unidade Sócio ${index + 1}: $unity" }.joinToString(";"),
            positions.mapIndexed { index: Int, position: String -> "Cargo Sócio ${index + 1}: $position" }.joinToString(";"),
            emails.mapIndexed { index: Int, email: String -> "Email Sócio ${index + 1}: $email" }.joinToString(";"),
            phones.mapIndexed { index: Int, phone: String -> "Telefone Sócio ${index + 1}: $phone" }.joinToString(";"),
            companyForm.totalPartners?.toString() ?: names.size.toString(),
            if (companyForm.hasUspPartners == true || nusps.any { it.isNotEmpty() }) "Sim" else "Não",
            if (companyForm.wantsToAddMorePartners == true) "Sim" else "Não",
            companyForm.cltEmployees.toString(),
            companyForm.pjCollaborators.toString(),
            companyForm.internsScholars.toString(),
            if (companyForm.hasInvestment) "Sim" else "Não",
            companyForm.investmentTypes.joinToString(";"),
            companyForm.ownInvestmentAmount ?: "",
            companyForm.angelInvestmentAmount ?: "",
            companyForm.ventureCapitalAmount ?: "",
            companyForm.privateEquityAmount ?: "",
            companyForm.pipeAmount ?: "",
            companyForm.otherInvestmentsAmount ?: "",
            companyForm.revenue2022 ?: "",
            companyForm.rfbSize ?: "",
            companyForm.totalSum?.toString() ?: "",
            companyForm.companyType ?: "",
            companyForm.operationalStatus ?: "",
            companyForm.index ?: "",
            companyForm.incubatorBond ?: "",
            companyForm.uspBondConfirmation ?: "",
            companyForm.uspDnaCategory ?: "",
            companyForm.companyBondConfirmation ?: ""
        )
    }
}
