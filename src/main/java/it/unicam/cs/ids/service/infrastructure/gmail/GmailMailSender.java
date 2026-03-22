package it.unicam.cs.ids.service.infrastructure.gmail;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.MailSender;

/**
 * Implementazione concreta di MailSender che utilizza l'API Gmail.
 * È l'adapter che collega il dominio al servizio esterno.
 */

public class GmailMailSender implements MailSender {

    private final Gmail gmail;

    /**
     * Costruttore: riceve un client Gmail già autenticato,
     * creato tramite GmailClientFactory.
     */

    public GmailMailSender(Gmail gmail) {
        this.gmail = gmail;
    }

    @Override
    public void inviaEmail(String destinatario, String oggetto, String corpo) {
        try {
            GmailApiClient.inviaEmail((com.google.api.services.gmail.Gmail) gmail, destinatario, oggetto, corpo);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'invio della mail a " + destinatario, e);
        }
    }
}
