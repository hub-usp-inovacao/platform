package br.usp.inovacao.hubusp.mailer

import java.io.File

class Mail(
    val to: List<String>,
    subject: String,
    val body: String,
    val cc: List<String> = emptyList(),
    val attachments: List<Attachment> = emptyList()
) {
    class Attachment(
        val name: String,
        val file: File,
    )

    val subject: String

    init {
        this.subject = formatSubject(subject)
    }

    private fun formatSubject(subject: String) = "[HubUSPInovação] - $subject"
}
