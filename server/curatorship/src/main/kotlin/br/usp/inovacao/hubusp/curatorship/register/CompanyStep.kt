package br.usp.inovacao.hubusp.curatorship.register
import java.io.StringWriter
import com.opencsv.CSVWriter
import kotlinx.serialization.Serializable
import org.valiktor.ConstraintViolationException
import org.valiktor.functions.isNotBlank
import org.valiktor.functions.isNotNull
import org.valiktor.i18n.mapToMessage
import org.valiktor.validate
import br.usp.inovacao.hubusp.curatorship.Configuration
import br.usp.inovacao.hubusp.curatorship.Mailer

@Serializable
data class CompanyStep(
    // TO DO
    //val data: CompanyDataStep,
    //val investment: InvestmentStep,
    //val revenue: RevenueStep,
    //val incubation: IncubationStep,
    //val dnaUsp: dnaUspStampStep,
    //val staff: StaffStep,
    //val partners: PartnerStep
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

    csvWriter.writeNext(flatData.keys.toTypedArray())
    csvWriter.writeNext(flatData.values.toTypedArray())
    csvWriter.close()

    val csvContent = writer.toString()

    Mailer(Configuration.EMAIL_USERNAME, Configuration.EMAIL_PASSWORD)
        .sendWithAttachment(
            to = "paraquedasshow@gmail.com",
            subject = "Dados da empresa",
            body = "Segue em anexo o CSV com os dados preenchidos.",
            attachmentName = "empresa.csv",
            attachmentContent = csvContent
        )
}