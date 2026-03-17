package it.unicam.cs.ids.service;

import it.unicam.cs.ids.controller.HackHandler;
import it.unicam.cs.ids.service.infrastructure.GmailMailSender;
import it.unicam.cs.ids.model.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.unicam.cs.ids.model.StaffIncompleto.INCOMPLETO;
import static it.unicam.cs.ids.model.Stato.BOZZA;

/**
 * Controller dei casi d'uso inerenti alla creazione e spedizione di inviti e alla gestione delle rispettive risposte.
 */

public class InvitiHandler {

    private final GmailMailSender gmailMailSender;
    private static InvitoService invitoService = new InvitoService();
    static NotificationService notificationService;

    public InvitiHandler(GmailMailSender gmailMailSender, InvitoService invitoService) {
        this.gmailMailSender = gmailMailSender;
        InvitiHandler.invitoService = invitoService;
    }


    public static void creaInvitoStaff(Utente daParteDi, Utente destinatario, Hackathon hackathon, RuoliStaff ruolo) {
        int giorniScadenza = Integer.parseInt(System.getenv("INVITO_SCAD_GG"));
        LocalDateTime scadenza = LocalDateTime.now().plusDays(giorniScadenza);
        InvitoStaff invito = new InvitoStaff(daParteDi, destinatario, hackathon, ruolo, scadenza);
        invitoService.salva(invito);
        notificationService.inviaInvito(invito);
    }

    //TODO accetta e rifiuta invito con relativa implementazione dei metodi

    public void rispostaInvito(Invito invito, boolean accetta) {
        switch (invito) {
            case InvitoStaff staff -> gestisciRispostaStaff(staff, accetta);
            case InvitoTeam team -> gestisciRispostaTeam(team, accetta);
            default -> throw new IllegalStateException("Tipo di invito non riconosciuto: " + invito);
        }
        invitoService.elimina(invito);
    }

    private void gestisciRispostaTeam(InvitoTeam team, boolean accetta) {
        // TODO: quando implementeremo il caso d'uso
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
     * Crea una lista degli inviti scaduti e li elimina, mandando una notifica all'organizzatore.
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


            if (hack.getStato() == BOZZA) {
                HackHandler.setStaffIncompleto(hack, INCOMPLETO);
                notificationService.inviaNotificaStaffIncompleto(hack, invitiScaduti);
            }

            invitiScaduti.forEach(notificationService::inviaNotificaInvitoScaduto);
            invitiScaduti.forEach(invitoService::elimina);
        }
    }


    private void gestisciRispostaGiudice(InvitoStaff staff, boolean accetta) {
        Hackathon hack = staff.getHackathon();
        // l'utente ha accettato di diventare giudice: viene aggiunto allo staff dell'hackathon
        if (accetta) {
            HackHandler.aggiungiGiudice(staff.getDestinatario(), hack);
            // l'utente non ha accettato di diventare giudice: lo staff di quell'hackathon è dichiarato incompleto
        } else {
            HackHandler.setStaffIncompleto(hack, INCOMPLETO);
            InvitoStaff stf = staff;
            notificationService.inviaNotificaStaffIncompleto(hack, (List<InvitoStaff>) staff);
        }
    }

    private void gestisciRispostaMentore(InvitoStaff staff, boolean accetta) {
        Hackathon hack = staff.getHackathon();
        if (accetta) {
            HackHandler.aggiungiMentore(staff.getDestinatario(), hack);
        } else {
            if (hack.getStato() == BOZZA) {
                int numMentori = Math.toIntExact(hack.getRuoli().stream()
                        .filter(rp -> rp.getTipoRuolo() == RuoliStaff.MENTORE)
                        .count());

                if (numMentori == 0) {
                    int numInvitiMentori = Math.toIntExact(invitoService.getInviti().stream()
                            .filter(inv -> inv instanceof InvitoStaff)
                            .map(inv -> (InvitoStaff) inv)
                            .filter(inv -> inv.getHackathon().equals(hack))
                            .filter(inv -> inv.getRuolo() == RuoliStaff.MENTORE)
                            .count());

                    // TODO: vedi sequence da "opt numInviti == 0, setStaffIncompleto
                }
            }
        }
    }
}


    // Crea il ruolo appropriato usando la factory
    RuoloPartecipazione nuovoRuolo;
    RoleFactory factory = new RoleFactory();

        switch (tipoRuolo) {
        case ORGANIZZATORE:
            nuovoRuolo = factory.assegnaOrganizzatore(utente, hackathon);
            break;
        case GIUDICE:
            nuovoRuolo = factory.assegnaGiudice(utente, hackathon);
            break;
        case MENTORE:
            nuovoRuolo = factory.assegnaMentore(utente, hackathon);
            break;
        default:
            throw new IllegalArgumentException("Tipo di ruolo non supportato: " + tipoRuolo);
    }

    // Aggiunge il ruolo alla lista
        hackathon.ruoli.add(nuovoRuolo);

// TODO: Implementare l'invio dell'invito tramite MailSender
// MailSender sender = new Gmail(); // o altra implementazione
// Invito invito = new InvitoStaff(utente, hackathon, tipoRuolo);
// sender.invia(invito);
