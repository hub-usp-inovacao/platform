package br.usp.inovacao.hubusp.curatorship

import java.util.Properties
import javax.mail.*
import javax.mail.internet.*
import javax.activation.DataSource
import javax.activation.DataHandler
import javax.mail.util.ByteArrayDataSource

class Mailer(private val username: String, private val password: String) {

    fun notifySpreadsheetError(message: String) {
        val to = Configuration.DEVS_EMAIL
        val subject = "Erro ao processar planilha"
        val body = message

        send(to, subject, body)
    }

    fun send(to: String, subject: String, body: String) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(username))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            setSubject(subject)
            setText(body)
        }

        Transport.send(message)
    }

    fun sendWithAttachment(
        to: String,
        subject: String,
        body: String,
        attachmentName: String,
        attachmentContent: String
    ) {
        val props = Properties().apply {
            put("mail.smtp.auth", "true")
            put("mail.smtp.starttls.enable", "true")
            put("mail.smtp.host", "smtp.gmail.com")
            put("mail.smtp.port", "587")
        }

        val session = Session.getInstance(props, object : Authenticator() {
            override fun getPasswordAuthentication(): PasswordAuthentication {
                return PasswordAuthentication(username, password)
            }
        })

        val message = MimeMessage(session).apply {
            setFrom(InternetAddress(username))
            setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
            setSubject(subject)

            val multipart = MimeMultipart()

            // Corpo do e-mail
            val textPart = MimeBodyPart()
            textPart.setText(body)
            multipart.addBodyPart(textPart)

            // Anexo CSV
            val attachmentPart = MimeBodyPart()
            val ds: DataSource = ByteArrayDataSource(attachmentContent.toByteArray(), "text/csv")
            attachmentPart.dataHandler = DataHandler(ds)
            attachmentPart.fileName = attachmentName
            multipart.addBodyPart(attachmentPart)

            setContent(multipart)
        }

        Transport.send(message)
    }
}