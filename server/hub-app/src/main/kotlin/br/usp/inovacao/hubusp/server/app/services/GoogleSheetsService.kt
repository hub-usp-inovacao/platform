package br.usp.inovacao.hubusp.server.app.services

import br.usp.inovacao.hubusp.server.app.models.CompanyForm
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

                    // Tentar parse como JSON
                    return try {
                        val scriptResponse = Json.decodeFromString(GoogleScriptResponse.serializer(), responseBody)
                        scriptResponse.status == "success"
                    } catch (e: Exception) {
                        // Se não for JSON, verificar se a resposta indica sucesso
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

        return mainHeaders + partnerHeaders
    }

    private fun createCompanyRow(companyForm: CompanyForm): List<String> {
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
            if (companyForm.dnaUspInfo.wantsSeal) "TRUE" else "FALSE",
            companyForm.dnaUspInfo.contactEmail ?: "",
            companyForm.dnaUspInfo.contactName ?: "",
            companyForm.dnaUspInfo.contactAgreements.joinToString(";"),
            companyForm.dnaUspInfo.category ?: "",
            companyForm.dnaUspInfo.confirmationStatus ?: "",
            companyForm.employeeInfo.cltEmployees.toString(),
            companyForm.employeeInfo.pjCollaborators.toString(),
            companyForm.employeeInfo.internsScholars.toString(),
            companyForm.employeeInfo.totalEmployees.toString(),
            if (companyForm.investmentInfo.hasInvestment) "TRUE" else "FALSE",
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
            if (companyForm.hasUspPartners == true) "TRUE" else "FALSE",
            companyForm.incubatorName ?: "",
            if (companyForm.isUnicorn == true) "TRUE" else "FALSE",
            companyForm.agreements.joinToString(";"),
            "Pendente"
        )

        val partnerValues = companyForm.partners?.flatMap { partner ->
            listOf(
                partner.name,
                partner.nusp ?: "",
                partner.bond ?: "",
                partner.unity ?: "",
                partner.email ?: "",
                partner.phone ?: "",
                partner.position ?: ""
            )
        } ?: emptyList()

        return mainValues + partnerValues
    }
}
