package br.usp.inovacao.hubusp.server.app.routing

import java.io.File
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText
import org.apache.tika.Tika
import org.owasp.html.HtmlPolicyBuilder

class ImageValidationException(
    message: String,
) : RuntimeException(message)

data class Image(var file: File) {
    val mimeType = Tika().detect(this.file) ?: ""
    val extension: String
    private val svgPolicy = HtmlPolicyBuilder().toFactory()

    init {
        val extension = Regex("image/([a-z]+)").find(this.mimeType)?.groupValues?.getOrNull(1)

        if (extension == null) {
            throw ImageValidationException("Image type not recognized: ${mimeType}")
        } else if (extension.contains("svg")) {
            val text = svgPolicy.sanitize(this.file.readText())
            this.file = createTempFile().apply { writeText(text, Charsets.UTF_8) }.toFile()
        }

        this.extension = extension
    }
}
