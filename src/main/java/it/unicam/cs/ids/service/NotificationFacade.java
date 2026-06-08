package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.*;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.inviti.Invito;
import it.unicam.cs.ids.model.inviti.InvitoHackathon;
import it.unicam.cs.ids.model.staff.RuoliStaff;
import it.unicam.cs.ids.model.team.MembroTeam;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Classe responsabile di creare l'oggetto e il corpo della mail da inviare, ed inviare la mail appena creata.
 */

//Pattern: Facade
@Service
public class NotificationFacade {

    private final MailSender mailSender;
    private final InvitoService invitoService;

    public NotificationFacade(MailSender mailSender, InvitoService invitoService) {
        this.mailSender = mailSender;
        this.invitoService = invitoService;
    }

    public void inviaInvito(Invito invito) {
        String oggetto = MailCreator.creaOggettoInvito(invito);
        String corpo = MailCreator.creaCorpoInvito(invito);
        mailSender.inviaEmail(invito.getDestinatario().getUtenteEmail(), oggetto, corpo);
        invitoService.salva(invito);
    }


    public void inviaNotificaInvitoAccettato(Invito invito) {
        String oggetto = MailCreator.creaOggettoInvitoAccettato(invito);
        String corpo = MailCreator.creaCorpoInvitoAccettato(invito);
        mailSender.inviaEmail(invito.getDestinatario().getUtenteEmail(), oggetto, corpo);
        invitoService.elimina(invito);
    }


    public void inviaNotificaInvitoRifiutato(Invito invito) {
        String oggetto = MailCreator.creaOggettoInvitoRifiutato(invito);
        String corpo = MailCreator.creaCorpoInvitoRifiutato(invito);
        mailSender.inviaEmail(invito.getDestinatario().getUtenteEmail(), oggetto, corpo);
        invitoService.elimina(invito);
    }


    public void inviaNotificaStaffIncompleto(Hackathon hack, List<InvitoHackathon> scaduti) {
        String oggetto = MailCreator.creaOggettoOrganizzatore(hack);
        String corpo = MailCreator.creaMessaggioStaffIncompleto(hack, scaduti);

        Utente organizzatore = hack.getRuoli().stream()
                .filter(rp -> rp.getTipoRuolo() == RuoliStaff.ORGANIZZATORE)
                .findFirst()
                .orElseThrow()
                .getUtente();

        mailSender.inviaEmail(organizzatore.getUtenteEmail(), oggetto, corpo);
    }

    public void inviaNotificaInvitoScaduto(InvitoHackathon invito) {
        String oggetto = MailCreator.creaOggettoInvito(invito);
        String corpo = MailCreator.creaMessaggioInvitoScaduto(invito);

        mailSender.inviaEmail(invito.getDestinatario().getUtenteEmail(), oggetto, corpo);
        invitoService.elimina(invito);
    }

    public void inviaNotificaHackathonConfermato(Hackathon hackathon) {
        String oggetto = MailCreator.creaOggettoHackathonConfermato(hackathon);
        String corpo = MailCreator.creaCorpoHackathonConfermato(hackathon);

        mailSender.inviaEmail(hackathon.getOrganizzatore().getUtenteEmail(), oggetto, corpo);
    }

    public void inviaNotificaAmministratore(MembroTeam membroTeam) {
        String oggetto = MailCreator.creaOggettoAmministratore(membroTeam);
        String corpo = MailCreator.creaCorpoAmministratore(membroTeam);

        mailSender.inviaEmail(membroTeam.getUtente().getUtenteEmail(), oggetto, corpo);
    }
}
