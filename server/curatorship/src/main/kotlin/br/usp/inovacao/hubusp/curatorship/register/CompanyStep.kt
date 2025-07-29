package br.usp.inovacao.hubusp.curatorship.register

import java.io.StringWriter
import java.io.File
import java.io.FileWriter
import com.opencsv.CSVWriter
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import br.usp.inovacao.hubusp.curatorship.Configuration
import br.usp.inovacao.hubusp.mailer.Mailer
import br.usp.inovacao.hubusp.mailer.Mail

@Serializable
data class CompanyStep(
    val about: AboutCompanyStep,
)

@Serializable
data class AboutCompanyStep(
    val description: String,
    val logo: String,
    val services: Set<String>,
    val technologies: Set<String>,
    val site: String,
    val odss: Set<String>,
    val socialMedia: Set<String>
) {
    init {
        try {
            validate(this) {
                validate(AboutCompanyStep::description)
                    .isNotNull()
                    .isNotBlank()
            }
        }
        catch (cve: ConstraintViolationException) {
            val violations: List<String> = cve.constraintViolations
                .mapToMessage(baseName = "ErrorMessages")
                .map { "${it.property} : ${it.message}" }
        }
    }
}

fun sendCompanyCsvEmail(companyData: Map<String, Any>) {
    try {
        println("=== INICIANDO ENVIO DE EMAIL ===")
        println("Dados da empresa: $companyData")

        val writer = StringWriter()
        val csvWriter = CSVWriter(writer)

        fun flattenMap(prefix: String, map: Map<*, *>): Map<String, String> {
            val result = mutableMapOf<String, String>()

            map.forEach { (key, value) ->
                val newKey = if (prefix.isEmpty()) key.toString() else "$prefix.$key"
                when (value) {
                    is Map<*, *> -> {
                        result.putAll(flattenMap(newKey, value))
                    }
                    is Collection<*> -> {
                        result[newKey] = value.joinToString(", ")
                    }
                    else -> {
                        result[newKey] = value?.toString() ?: ""
                    }
                }
            }
            return result
        }

        val flatData = flattenMap("", companyData)
        println("Dados achatados: $flatData")

        csvWriter.writeNext(flatData.keys.toTypedArray())
        csvWriter.writeNext(flatData.values.toTypedArray())
        csvWriter.close()

        val csvContent = writer.toString()
        println("CSV gerado com ${csvContent.length} caracteres")

        val tempFile = File.createTempFile("empresa_dados", ".csv")
        println("Arquivo temporário criado: ${tempFile.absolutePath}")

        FileWriter(tempFile).use { fileWriter ->
            fileWriter.write(csvContent)
        }
        println("Conteúdo escrito no arquivo")

        println("EMAIL_USERNAME: ${Configuration.EMAIL_USERNAME}")
        println("EMAIL_PASSWORD existe: ${Configuration.EMAIL_PASSWORD.isNotEmpty()}")

        val mailer = Mailer(Configuration.EMAIL_USERNAME, Configuration.EMAIL_PASSWORD)
        println("Mailer criado")

        val mail = Mail(
            to = listOf("paraquedasshow@gmail.com"),
            subject = "Dados da empresa - Formulário preenchido",
            body = "Segue em anexo o arquivo CSV com os dados da empresa preenchidos no formulário.\n\nEste email foi enviado automaticamente pelo sistema Hub USP Inovação.",
            attachments = listOf(
                Mail.Attachment(
                    name = "dados_empresa.csv",
                    file = tempFile
                )
            )
        )
        println("Mail object criado")

        mailer.send(mail)
        println("Email enviado com sucesso!")

        tempFile.delete()
        println("Arquivo temporário removido")

    } catch (e: Exception) {
        println("ERRO ao enviar email: ${e.message}")
        e.printStackTrace()
        throw e
    }
}