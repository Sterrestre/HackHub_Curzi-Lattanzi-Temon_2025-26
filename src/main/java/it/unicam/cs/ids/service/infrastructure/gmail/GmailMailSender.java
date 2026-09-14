package it.unicam.cs.ids.service.infrastructure.gmail;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.MailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * Implementazione concreta di MailSender che utilizza l'API Gmail.
 * È l'adapter che collega il dominio al servizio esterno.
 */

// Pattern: Adapter
@Service
@ConditionalOnProperty(name = "gmail.enabled", havingValue = "true")
public class GmailMailSender implements MailSender {

    private final Gmail gmail;
    private final String mittente;

    /**
     * Costruttore: riceve un client Gmail già autenticato,
     * creato tramite GmailClientFactory.
     */

    public GmailMailSender(Gmail gmail, @Value("${GMAIL_SENDER_EMAIL:noreply@unicam.it}") String mittente) {
        this.gmail = gmail;
        this.mittente = mittente;
    }

    @Override
    public void inviaEmail(String destinatario, String oggetto, String corpo) {
        try {
//            GmailApiClient.inviaEmail(gmail, destinatario, oggetto, corpo, mittente);
            GmailApiClient.inviaEmail(gmail, destinatario, oggetto, corpo);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'invio della mail a " + destinatario, e);
        }
    }
}
