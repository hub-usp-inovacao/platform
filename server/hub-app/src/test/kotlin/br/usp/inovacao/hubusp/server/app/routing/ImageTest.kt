package br.usp.inovacao.hubusp.server.app.routing

import java.io.File
import kotlin.io.path.createTempFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ImageTest {
    @Test
    fun `it fails when file is NOT an image`() {
        val image = Image(createTempFile().toFile())
        assertFailsWith<ImageValidationException> { image.validate() }
    }

    @Test
    fun `it does NOT fail when file IS image`() {
        val image = Image(File(this::class.java.getResource("/routing/logo.webp").toURI()))
        image.validate()
    }

    @Test
    fun `it identifies webp`() {
        val image = Image(File(this::class.java.getResource("/routing/logo.webp").toURI()))

        assertEquals("image/webp", image.mimeType)
        assertEquals("webp", image.extension)
    }
}
