package br.usp.inovacao.hubusp.server.app.routing

import java.io.File
import org.apache.tika.Tika
import org.owasp.html.HtmlPolicyBuilder

class ImageValidationException(
    message: String,
) : RuntimeException(message)

data class Image(var file: File) {
    val mimeType = Tika().detect(this.file) ?: ""
    val extension = Regex("image/([a-z]+)").find(this.mimeType)?.groupValues?.getOrNull(1)
    private val svgPolicy = HtmlPolicyBuilder().toFactory()

    init {
        if (this.extension == null) {
            throw ImageValidationException("Image type not recognized: ${mimeType}")
        } else if (extension.contains("svg")) {
            // TODO: Basic SVG sanitization
        }
    }
}
