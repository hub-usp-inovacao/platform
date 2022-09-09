package br.usp.inovacao.hubusp.mailer

import java.util.*
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class Mailer(
    private val user: String,
    private val password: String
) {
    private val protocol = "smtp"
    private val host = "smtp.gmail.com"

    private val auth = object : Authenticator() {
        override fun getPasswordAuthentication() =
            PasswordAuthentication(user, password)
    }

    private val config = Properties()

    init {
        with(config) {
            put("mail.smtp.host", host)
            put("mail.smtp.port", "587")
            put("mail.smtp.auth", true)
            put("mail.smtp.starttls.enable", true)
            put("mail.smtp.ssl.protocols", "TLSv1.2")
        }
    }

    private fun buildMessage(session: Session, mail: Mail): MimeMessage {
        val message = MimeMessage(session)
        with(message) {
            setFrom(user)
            mail.to.forEach { addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(it)) }
            mail.ccs.forEach { addRecipient(javax.mail.Message.RecipientType.BCC, InternetAddress(it)) }
            subject = mail.subject
            setText(mail.body)
        }
        return message
    }

    fun send(mail: Mail) {
        val session = Session.getInstance(config, auth)
        val message = buildMessage(session, mail)
        val transport = session.getTransport(protocol)
        with(transport) {
            connect(host, user, password)
            sendMessage(message, message.allRecipients)
            close()
        }
    }
}