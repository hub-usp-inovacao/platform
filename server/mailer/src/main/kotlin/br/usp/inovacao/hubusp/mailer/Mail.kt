package br.usp.inovacao.hubusp.mailer

class Mail(
    val to: List<String>,
    subject: String,
    val body: String,
    val ccs: List<String> = emptyList(),
) {
    val subject: String

    init {
        this.subject = formatSubject(subject)
    }

    private fun formatSubject(subject: String) = "[HubUSPInovação] - $subject"
}