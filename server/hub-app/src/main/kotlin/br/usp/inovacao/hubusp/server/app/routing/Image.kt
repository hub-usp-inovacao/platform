package br.usp.inovacao.hubusp.server.app.routing

import java.io.File
import org.apache.tika.Tika

class ImageValidationException(
    mimeType: String,
) : RuntimeException("Image type not recognized: ${mimeType}")

data class Image(val file: File) {
    val mimeType = Tika().detect(this.file) ?: ""
    val extension = Regex("image/([a-z]+)").find(this.mimeType)?.groupValues?.getOrNull(1)

    fun validate() {
        if (extension == null) {
            throw ImageValidationException(mimeType)
        } else if (extension.contains("svg")) {
            // TODO: Basic SVG sanitization
        }
    }
}
