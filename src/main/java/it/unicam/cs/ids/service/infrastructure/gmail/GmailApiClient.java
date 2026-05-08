package it.unicam.cs.ids.service.infrastructure.gmail;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;

/**
 * Classe di utilità che incapsula la logica tecnica per inviare email tramite Gmail API.
 * Non è visibile al dominio: viene usata solo da GmailMailSender.
 */
public class GmailApiClient {

    /**
     * Invia una email tramite Gmail API.
     *
     * @param gmail        client Gmail autenticato
     * @param destinatario indirizzo email del destinatario
     * @param oggetto      oggetto della mail
     * @param corpo        corpo della mail
     */
    public static void inviaEmail(Gmail gmail, String destinatario, String oggetto, String corpo) throws Exception {
        MimeMessage mimeMessage = creaMimeMessage(destinatario, oggetto, corpo);
        Message gmailMessage = creaGmailMessage(mimeMessage);

        gmail.users()
                .messages()
                .send("me", gmailMessage)
                .execute();
    }

    /**
     * Crea un MimeMessage standard JavaMail.
     */
    private static MimeMessage creaMimeMessage(String destinatario, String oggetto, String corpo) throws Exception {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("me")); // Gmail sostituirà automaticamente con l'account OAuth
        message.addRecipient(jakarta.mail.Message.RecipientType.TO, new InternetAddress(destinatario));
        message.setSubject(oggetto);
        message.setText(corpo);

        return message;
    }

    /**
     * Converte un MimeMessage in un Message compatibile con Gmail API.
     */
    private static Message creaGmailMessage(MimeMessage mimeMessage) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        mimeMessage.writeTo(buffer);

        String encodedEmail = Base64.getUrlEncoder().encodeToString(buffer.toByteArray());

        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }
}