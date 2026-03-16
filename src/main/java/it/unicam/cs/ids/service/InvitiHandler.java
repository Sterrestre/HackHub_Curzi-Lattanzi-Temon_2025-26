package it.unicam.cs.ids.service;

import it.unicam.cs.ids.controller.HackHandler;
import it.unicam.cs.ids.infrastructure.MailSender;
import it.unicam.cs.ids.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Controller dei casi d'uso inerenti alla creazione e spedizione di inviti e alla gestione delle rispettive risposte.
 */

public class InvitiHandler {

    private final MailSender mailSender;
    private final InvitoService invitoService;

    public InvitiHandler(MailSender mailSender, InvitoService invitoService) {
        this.mailSender = mailSender;
        this.invitoService = invitoService;
    }


    public InvitoStaff creaInvitoStaff(Utente daParteDi, Utente destinatario, Hackathon hackathon, RuoliStaff ruolo) {
        int giorniScadenza = Integer.parseInt(System.getenv("INVITO_SCAD_GG"));
        LocalDateTime scadenza = LocalDateTime.now().plusDays(giorniScadenza);
        InvitoStaff invito = new InvitoStaff(daParteDi, destinatario, hackathon, ruolo, scadenza);

        String oggetto = MailCreator.creaOggettoInvito(invito);
        String corpo = MailCreator.creaCorpoInvito(invito);

        invitoService.salva(invito);
        mailSender.inviaEmail(invito);

        return invito;
    }

    //TODO accetta e rifiuta invito con relativa implementazione dei metodi

    public void rispostaInvito(Invito invito, boolean accetta) {
        switch (invito) {
            case InvitoStaff staff -> gestisciRispostaStaff(staff, accetta);
        }
        invitoService.elimina(invito);
    }


//  Il caso d'uso Risposta membro staff viene attivato dall'utente invitato come membro di staff (ha risposto all'invito).
//  La risposta viene gestita diversamente in base al ruolo per cui l'utente era stato invitato, se come giudice o come mentore.
    private void gestisciRispostaStaff(InvitoStaff staff, boolean accetta) {
        RuoliStaff ruolo = staff.getRuolo();

        switch (ruolo) {
            case GIUDICE -> gestisciRispostaGiudice(staff, accetta);
            case MENTORE -> gestisciRispostaMentore(staff, accetta);
        }
    }


    /**
     * Metodo che verifica se la scadenza dell'invito per un membro dello staff è scaduto.
     * Crea una lista degli inviti scaduti e li elimina, mandato una notifica all'organizzatore.
     * Qualora l'hackathon per cui era stato mandato l'invito sia ancora in stato di BOZZA, avvisa l'organizzatore.
     */
    public void verificaScadenze() {

        List<InvitoStaff> scaduti = invitoService.getInviti().stream()
                .filter(InvitoStaff.class::isInstance)
                .map(InvitoStaff.class::cast)
                .filter(i -> i.getScadenza().isBefore(LocalDateTime.now()))
                .toList();


        Map<Hackathon, List<InvitoStaff>> perHackathon =
                scaduti.stream().collect(Collectors.groupingBy(InvitoStaff::getHackathon));

        for (Map.Entry<Hackathon, List<InvitoStaff>> entry : perHackathon.entrySet()) {
            Hackathon hack = entry.getKey();
            List<InvitoStaff> invitiScaduti = entry.getValue();


            if (hack.getStato() == HackState.BOZZA) {
                //TODO setStaffIncompleto + manda notifica
                String messaggio = MailCreator.creaMessaggioStaffIncompleto(hack, invitiScaduti);
                mailSender.inviaEmail();
            }

            perHackathon.getHackathon(hack).forEach(invitoService::elimina);
        }
    }

//  Il caso d'uso Risposta membro staff viene attivato dal tempo (gli inviti sono scaduti).



    private void gestisciRispostaGiudice(InvitoStaff staff, boolean accetta) {
        // l'utente ha accettato di diventare giudice: viene aggiunto allo staff dell'hackathon
        if (accetta) {
            HackHandler.aggiungiGiudice(Utente staff.getDestinatario(), Hackathon staff.getHackathon());
        // l'utente non ha accettato di diventare giudice: lo staff di quell'hackathon è dichiarato incompleto
        } else {
            // TODO: vedi sequence da "staff incompleto"
        }
    }

    private void gestisciRispostaMentore(InvitoStaff staff, boolean accetta) {
        Hackathon hack = staff.getHackathon();
        if (accetta) {
            HackHandler.aggiungiMentore(Utente staff.getDestinatario(), Hackathon hack);
        } else {
            if (hack.getStato() == Stato.BOZZA) {
                int numMentori = hack.getRuoli().stream()
                        .filter(rp -> rp.getTipoRuolo() == RuoliStaff.MENTORE)
                        .count();

                if (numMentori == 0) {
                    int numInvitiMentori = invitoService.getInviti().stream()
                            .filter(inv -> inv instanceof InvitoStaff)
                            .map(inv -> (InvitoStaff) inv)
                            .filter(inv -> inv.getHackathon().equals(hack))
                            .filter(inv -> inv.getRuolo() == RuoliStaff.MENTORE)
                            .count();

                    // TODO: vedi sequence da "opt numInviti == 0, setStaffIncompleto
                }
            }
        }
    }

}
