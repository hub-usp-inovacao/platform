package br.usp.inovacao.hubusp.server.app.routing

import br.usp.inovacao.hubusp.config.Configuration
import br.usp.inovacao.hubusp.mailer.Mail
import br.usp.inovacao.hubusp.mailer.Mailer
import com.mongodb.client.MongoDatabase
import com.mongodb.client.model.Filters.eq
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.PartData
import io.ktor.http.content.forEachPart
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.log
import io.ktor.server.request.queryString
import io.ktor.server.request.receive
import io.ktor.server.request.receiveMultipart
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import java.io.File
import java.util.Base64
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import org.bson.Document

fun Application.configureCompatibilityRoute(db: MongoDatabase, mailer: Mailer) {
    val companies = db.getCollection("companies", Document::class.java)
    val skills = db.getCollection("skills", Document::class.java)
    val conexoes = db.getCollection("conexoes", Document::class.java)
    val companyUpdates = db.getCollection("company_update_requests", Document::class.java)
    val skillUpdates = db.getCollection("skill_update_requests", Document::class.java)

    routing {
        get("/iniciatives") {
            val suffix = call.request.queryString().takeIf { it.isNotBlank() }?.let { "?$it" } ?: ""
            call.respondRedirect("/api/initiatives$suffix")
        }

        post("/company-data") {
            val body = call.receive<JsonObject>()
            val token = body["security"]?.jsonObject?.string("token")
            val cnpj = token?.let { TokenService.verify(it) }?.get("cnpj")

            if (cnpj == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid token"))
                return@post
            }

            val company = companies.find(eq("cnpj", cnpj)).first()
            if (company == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "company not found"))
                return@post
            }

            call.respondText(company.toJson(), ContentType.Application.Json, HttpStatusCode.OK)
        }

        post("/skills") {
            val body = call.receive<JsonObject>()
            val token = body["skill"]?.jsonObject?.string("token")
            val email = token?.let { TokenService.verify(it) }?.get("email")

            if (email == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid token"))
                return@post
            }

            val skill = skills.find(eq("email", email)).first()
            if (skill == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "skill not found"))
                return@post
            }

            call.respondText(skill.toJson(), ContentType.Application.Json, HttpStatusCode.OK)
        }

        post("/companies/update_request") {
            val body = call.receive<JsonObject>()
            val cnpj = body["update_request"]?.jsonObject?.string("cnpj")

            if (cnpj.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "cnpj is required"))
                return@post
            }

            val company = companies.find(eq("cnpj", cnpj)).first()
            if (company == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "company not found"))
                return@post
            }

            val email = company.getStringList("emails").firstOrNull()
            if (email.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "company email not found"))
                return@post
            }

            val token = TokenService.create(mapOf("cnpj" to cnpj))
            sendMail(
                mailer,
                Mail(
                    to = setOf(email),
                    cc = Configuration.email.cc,
                    subject = "Token de seguranca para atualizacao - Hub USPInovacao",
                    body = "Use este token para atualizar os dados da empresa: $token",
                ),
            )

            call.respond(HttpStatusCode.OK, mapOf("message" to "ok", "email" to email.maskEmail()))
        }

        patch("/companies") {
            val body = call.receive<JsonObject>()
            val company = body["company"]?.jsonObject

            if (company == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("errors" to mapOf("company" to listOf("is required"))))
                return@patch
            }

            val document = Document.parse(company.toString())
            document["timestamp"] = System.currentTimeMillis()
            document["delivered"] = false
            companyUpdates.insertOne(document)

            val email = company["company_data"]?.jsonObject?.stringList("emails")?.firstOrNull()
            if (!email.isNullOrBlank()) {
                sendMail(
                    mailer,
                    Mail(
                        to = setOf(email),
                        cc = Configuration.email.cc,
                        subject = "Confirmacao de Atualizacao de Empresa",
                        body = "Recebemos a solicitacao de atualizacao dos dados da empresa.",
                    ),
                )
            }

            call.respond(HttpStatusCode.OK, mapOf("company_update" to mapOf("id" to document["_id"].toString())))
        }

        post("/companies/update_request/logo") {
            val multipart = call.receiveMultipart()
            var cnpj = ""
            var stored = false

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> if (part.name == "company[cnpj]") cnpj = part.value.onlyDigits()
                    is PartData.FileItem -> {
                        val extension = File(part.originalFileName ?: "logo").extension.ifBlank { "bin" }
                        val dir = File("uploads/logos").apply { mkdirs() }
                        val file = File(dir, "${cnpj.ifBlank { "logo" }}.$extension")
                        part.provider().copyAndClose(file.writeChannel())
                        stored = true
                    }
                    else -> {}
                }
                part.dispose()
            }

            if (stored) {
                call.respond(HttpStatusCode.OK, mapOf("message" to "Logo uploaded successfully"))
            } else {
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to "Logo is required"))
            }
        }

        post("/skills/update_request") {
            val body = call.receive<JsonObject>()
            val email = body["update_request"]?.jsonObject?.string("email")

            if (email.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "email is required"))
                return@post
            }

            val skill = skills.find(eq("email", email)).first()
            if (skill == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "skill not found"))
                return@post
            }

            val token = TokenService.create(mapOf("email" to email))
            sendMail(
                mailer,
                Mail(
                    to = setOf(email),
                    cc = Configuration.email.cc,
                    subject = "Token de seguranca para atualizacao - Hub USPInovacao",
                    body = "Use este token para atualizar os dados da competencia: $token",
                ),
            )

            call.respond(HttpStatusCode.OK, mapOf("message" to "ok"))
        }

        patch("/skills") {
            val body = call.receive<JsonObject>()
            val skill = body["skill"]?.jsonObject ?: body
            val document = Document.parse(skill.toString())
            document["delivered"] = false
            skillUpdates.insertOne(document)

            call.respond(HttpStatusCode.Created, mapOf("request" to mapOf("id" to document["_id"].toString())))
        }

        post("/conexao") {
            val body = call.receive<JsonObject>()
            val conexao = body["conexao"]?.jsonObject

            if (conexao == null || !conexao.hasKeys("requestId", "personal", "org", "demand")) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "invalid conexao"))
                return@post
            }

            val document = Document.parse(conexao.toString())
            document["delivered"] = false
            document["images"] = emptyList<Document>()
            conexoes.insertOne(document)

            val authorEmail = conexao["personal"]?.jsonObject?.string("email")
            if (!authorEmail.isNullOrBlank()) {
                sendMail(
                    mailer,
                    Mail(
                        to = setOf(authorEmail),
                        cc = Configuration.email.cc,
                        subject = "Confirmacao - ConexaoUSP",
                        body = "Recebemos sua solicitacao no Conexao USP.",
                    ),
                )
            }

            call.respond(HttpStatusCode.OK, mapOf("conexao" to mapOf("id" to document["_id"].toString())))
        }

        post("/conexao/image") {
            val multipart = call.receiveMultipart()
            var requestId = ""
            var image: File? = null

            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> if (part.name == "requestId") requestId = part.value
                    is PartData.FileItem -> {
                        val file = File.createTempFile("conexao-image-", ".bin")
                        part.provider().copyAndClose(file.writeChannel())
                        image = file
                    }
                    else -> {}
                }
                part.dispose()
            }

            val file = image
            if (requestId.isBlank() || file == null) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "requestId and image are required"))
                return@post
            }

            val encoded = Base64.getEncoder().encodeToString(file.readBytes())
            conexoes.updateOne(eq("requestId", requestId), Document("\$push", Document("images", Document("content", encoded))))
            call.respond(HttpStatusCode.Created)
        }

        get("/areas") {
            call.respond(HttpStatusCode.OK, mapOf("areas" to KNOWLEDGE_AREAS))
        }

        get("/campi") {
            call.respond(HttpStatusCode.OK, mapOf("campi" to CAMPI.map { it.name }))
        }

        get("/unities") {
            call.respond(HttpStatusCode.OK, mapOf("unities" to CAMPI.flatMap { it.unities }.sorted()))
        }
    }
}

private fun Application.sendMail(mailer: Mailer, mail: Mail) =
    try {
        mailer.send(mail)
    } catch (e: Exception) {
        log.warn("Failed to send email: ${e.message}")
    }

private fun JsonObject.string(key: String): String? = (this[key] as? JsonPrimitive)?.contentOrNull

private fun JsonObject.stringList(key: String): List<String> =
    when (val value = this[key]) {
        is JsonPrimitive -> listOfNotNull(value.contentOrNull)
        else -> value?.toString()
            ?.removeSurrounding("[", "]")
            ?.split(",")
            ?.map { it.trim().trim('"') }
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

private fun JsonObject.hasKeys(vararg keys: String) = keys.all { containsKey(it) }

private fun String.onlyDigits() = filter { it.isDigit() }

private fun String.maskEmail(): String {
    val at = indexOf('@')
    if (at <= 0) return this

    val hidden =
        when (at) {
            1, 2 -> 1
            3, 4 -> 2
            5, 6 -> 3
            else -> 5
        }

    return "${"*".repeat(hidden)}${substring(at)}"
}

private fun Document.getStringList(key: String): List<String> =
    when (val value = this[key]) {
        is List<*> -> value.filterIsInstance<String>()
        is String -> listOf(value)
        else -> emptyList()
    }

@Serializable private data class Campus(val name: String, val unities: List<String>)

@Serializable private data class KnowledgeArea(val name: String, val subareas: List<String>)

private val CAMPI =
    listOf(
        Campus("On-line", listOf("Pro-Reitoria de Graduacao")),
        Campus("Bauru", listOf("Faculdade de Odontologia de Bauru - FOB", "Hospital de Reabilitacao de Anomalias Craniofaciais - HRAC")),
        Campus("Butanta", listOf("Escola Politecnica - EP", "Instituto de Fisica - IF", "Instituto de Matematica e Estatistica - IME")),
        Campus("Lorena", listOf("Escola de Engenharia de Lorena - EEL")),
        Campus("Piracicaba", listOf("Centro de Energia Nuclear na Agricultura - CENA", "Escola Superior de Agricultura Luiz de Queiroz - ESALQ")),
        Campus("Pirassununga", listOf("Faculdade de Zootecnia e Engenharia de Alimentos - FZEA")),
        Campus("Ribeirao Preto", listOf("Faculdade de Medicina de Ribeirao Preto - FMRP", "Faculdade de Economia, Administracao e Contabilidade de Ribeirao Preto - FEARP")),
        Campus("Sao Carlos", listOf("Escola de Engenharia de Sao Carlos - EESC", "Instituto de Ciencias Matematicas e de Computacao - ICMC")),
        Campus("USP Leste", listOf("Escola de Artes, Ciencias e Humanidades - EACH")),
    )

private val KNOWLEDGE_AREAS =
    listOf(
        KnowledgeArea("Ciencias Agrarias", listOf("Agronomia", "Zootecnia", "Medicina Veterinaria")),
        KnowledgeArea("Ciencias Biologicas", listOf("Biologia Geral", "Genetica", "Ecologia")),
        KnowledgeArea("Ciencias da Saude", listOf("Medicina", "Odontologia", "Farmacia", "Enfermagem")),
        KnowledgeArea("Ciencias Exatas e da Terra", listOf("Matematica", "Ciencia da Computacao", "Fisica", "Quimica")),
        KnowledgeArea("Engenharias", listOf("Engenharia Civil", "Engenharia Eletrica", "Engenharia Mecanica", "Engenharia Quimica")),
        KnowledgeArea("Ciencias Humanas", listOf("Filosofia", "Sociologia", "Historia", "Educacao")),
        KnowledgeArea("Ciencias Sociais Aplicadas", listOf("Direito", "Administracao", "Economia", "Comunicacao")),
        KnowledgeArea("Linguistica, Letras e Artes", listOf("Linguistica", "Letras", "Artes")),
    )
