package it.unicam.cs.ids.infrastructure;

import it.unicam.cs.ids.model.Invito;
import it.unicam.cs.ids.service.InvitoSender;
import it.unicam.cs.ids.service.MailCreator;

public class MailSender implements InvitoSender {

    private final Gmail gmail;

    public MailSender(Gmail gmail) {
        this.gmail = gmail;
    }

    @Override
    public void inviaEmail(Invito invito) {
        String oggetto = MailCreator.creaOggettoInvito(invito);
        String corpo = MailCreator.creaCorpo(invito);
        gmail.inviaEmail(invito.getDestinatario().getEmail(), oggetto, corpo);
    }
}
