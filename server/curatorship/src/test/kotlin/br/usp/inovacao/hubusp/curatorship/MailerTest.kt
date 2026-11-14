package br.usp.inovacao.hubusp.curatorship

import br.usp.inovacao.hubusp.mailer.Mail
import br.usp.inovacao.hubusp.mailer.Mailer as CoreMailer
import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.unmockkAll
import kotlin.test.AfterTest
import kotlin.test.Test

class MailerTest {
    @AfterTest
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `it sends spreadsheet error notifications`() {
        mockkConstructor(CoreMailer::class)
        every { anyConstructed<CoreMailer>().send(any<Mail>()) } returns Unit

        val mailer = Mailer("user", "password")
        mailer.notifySpreadsheetError("message")
    }
}
