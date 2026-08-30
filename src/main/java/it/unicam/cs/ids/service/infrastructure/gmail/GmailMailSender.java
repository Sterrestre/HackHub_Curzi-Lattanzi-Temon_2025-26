package it.unicam.cs.ids.service.infrastructure.gmail;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.MailSender;

/**
 * Implementazione concreta di MailSender che utilizza l'API Gmail.
 * È l'adapter che collega il dominio al servizio esterno.
 */

// Pattern: Adapter
public class GmailMailSender implements MailSender {

// SE DISTINZIONE PROD E DEV
    private final Gmail gmail;
//    private final String mittente;

    /**
     * Costruttore: riceve un client Gmail già autenticato,
     * creato tramite GmailClientFactory.
     */

// SE DISTINZIONE PROD E DEV
//    public GmailMailSender(Gmail gmail, String mittente) {
//        this.gmail = gmail;
//        this.mittente = mittente;
//    }

    public GmailMailSender(Gmail gmail) {
        this.gmail = gmail;
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
