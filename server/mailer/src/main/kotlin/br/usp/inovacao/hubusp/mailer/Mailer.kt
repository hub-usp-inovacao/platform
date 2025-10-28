package br.usp.inovacao.hubusp.mailer

import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class Mailer(private val user: String, private val password: String) {
    private val protocol = "smtp"
    private val host = "smtp.gmail.com"

    private val auth =
        object : Authenticator() {
            override fun getPasswordAuthentication() = PasswordAuthentication(user, password)
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

            mail.to.forEach {
                addRecipient(javax.mail.Message.RecipientType.TO, InternetAddress(it))
            }

            mail.cc.forEach {
                addRecipient(javax.mail.Message.RecipientType.CC, InternetAddress(it))
            }

            mail.bcc.forEach {
                addRecipient(javax.mail.Message.RecipientType.BCC, InternetAddress(it))
            }

            setSubject(mail.subject)
        }

        if (mail.attachments.isEmpty()) {
            message.setText(mail.body)
        } else {
            val multipart = MimeMultipart()

            multipart.addBodyPart(MimeBodyPart().apply { setText(mail.body) })

            mail.attachments.forEach {
                multipart.addBodyPart(
                    MimeBodyPart().apply {
                        attachFile(it.file)
                        setFileName(it.name)
                    })
            }

            message.setContent(multipart)
        }

        return message
    }

    // TODO: Catch error inside Mailer to avoid possible DoS.
    // If the mail fails to send for whatever reason this throws an error.
    // We should catch it and return some Status object.
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
