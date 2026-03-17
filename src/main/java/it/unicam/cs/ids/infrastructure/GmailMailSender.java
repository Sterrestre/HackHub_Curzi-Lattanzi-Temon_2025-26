package it.unicam.cs.ids.infrastructure;

import it.unicam.cs.ids.service.MailSender;
import it.unicam.cs.ids.service.MailCreator;

/**
 * Adapter per inviare mail tramite il sistema Gmail.
 */
public class GmailMailSender implements MailSender {

    private final Gmail gmail;

    public GmailMailSender(Gmail gmail) {
        this.gmail = gmail;
    }

    @Override
    public void inviaEmail(String destinatario, String oggetto, String corpo) {
        try {
            GmailApiClient.inviaEmail(gmail, destinatario, oggetto, corpo);
        } catch (Exception e) {
            throw new RuntimeException("Errore durante l'invio della mail a " + destinatario, e);
        }
        String oggetto = MailCreator.creaOggettoInvito(invito);
        String corpo = MailCreator.creaCorpo(invito);
        gmail.inviaEmail(invito.getDestinatario().getEmail(), oggetto, corpo);
    }
}
