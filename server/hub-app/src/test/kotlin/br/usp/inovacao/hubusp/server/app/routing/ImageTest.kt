package br.usp.inovacao.hubusp.server.app.routing

import java.io.File
import kotlin.io.path.createTempFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ImageTest {
    @Test
    fun `it fails when file is NOT an image`() {
        assertFailsWith<ImageValidationException> { Image(createTempFile().toFile()) }
    }

    @Test
    fun `it does NOT fail when file IS image`() {
        Image(File(this::class.java.getResource("/routing/logo.webp").toURI()))
    }

    @Test
    fun `it identifies webp`() {
        val image = Image(File(this::class.java.getResource("/routing/logo.webp").toURI()))

        assertEquals("image/webp", image.mimeType)
        assertEquals("webp", image.extension)
    }

    @Test
    fun `it sanitizes svg`() {
        val image = Image(File(this::class.java.getResource("/routing/maliciousLogo.svg").toURI()))

        assert(image.mimeType.contains("image/svg"))
        assertEquals("svg", image.extension)
        assert(!image.file.readText().contains("script"))
    }
}
