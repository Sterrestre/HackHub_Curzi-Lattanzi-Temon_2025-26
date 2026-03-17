package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.*;

import java.util.List;

/**
 * Classe responsabile di creare l'oggetto e il corpo della mail da inviare, ed inviare la mail appena creata.
 */
public class NotificationService {

    private final MailSender mailSender;

    public NotificationService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void inviaInvito(Invito invito) {
        String oggetto = MailCreator.creaOggettoInvito(invito);
        String corpo = MailCreator.creaCorpoInvito(invito);
        mailSender.inviaEmail(invito.getDestinatario().getUtenteEmail(), oggetto, corpo);
    }

    public void inviaNotificaStaffIncompleto(Hackathon hack, List<InvitoStaff> scaduti) {
        String oggetto = MailCreator.creaOggettoOrganizzatore(hack);
        String corpo = MailCreator.creaMessaggioStaffIncompleto(hack, scaduti);

        Utente organizzatore = hack.getRuoli().stream()
                .filter(rp -> rp.getTipoRuolo() == RuoliStaff.ORGANIZZATORE)
                .findFirst()
                .orElseThrow()
                .getUtente();

        mailSender.inviaEmail(organizzatore.getUtenteEmail(), oggetto, corpo);
    }

    public void inviaNotificaInvitoScaduto(InvitoStaff invito) {
        String oggetto = MailCreator.creaOggettoInvito(invito);
        String corpo = MailCreator.creaMessaggioInvitoScaduto(invito);

        mailSender.inviaEmail(invito.getDestinatario().getUtenteEmail(), oggetto, corpo);
    }
}
