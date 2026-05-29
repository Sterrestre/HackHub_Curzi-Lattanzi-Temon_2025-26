package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.ConfermatoState;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.inviti.Invito;
import it.unicam.cs.ids.model.inviti.InvitoHackathon;
import it.unicam.cs.ids.model.inviti.InvitoTeam;
import it.unicam.cs.ids.model.staff.RoleFactory;
import it.unicam.cs.ids.model.staff.RuoliStaff;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.service.InvitoService;
import it.unicam.cs.ids.service.NotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.unicam.cs.ids.model.hackathon.StaffIncompleto.INCOMPLETO;
import static it.unicam.cs.ids.model.hackathon.Stato.BOZZA;

/**
 * Controller dei casi d'uso inerenti alla creazione e spedizione di inviti e alla gestione delle rispettive risposte.
 */
@Service
public class InvitiHandler {

    private final InvitoService invitoService;
    private final NotificationService notificationService;
    private final RoleFactory roleFactory;


    public InvitiHandler(InvitoService invitoService, NotificationService notificationService, RoleFactory roleFactory) {
        this.invitoService = invitoService;
        this.notificationService = notificationService;
        this.roleFactory = roleFactory;
    }


    public void creaInvitoStaff(Utente organizzatore, Utente destinatario, Hackathon hackathon, RuoliStaff ruolo) {
        int giorniScadenza = Integer.parseInt(System.getenv("INVITO_SCAD_GG"));
        LocalDateTime scadenza = LocalDateTime.now().plusDays(giorniScadenza);
        InvitoHackathon invito = new InvitoHackathon(organizzatore, destinatario, hackathon, ruolo, scadenza);
        invitoService.salva(invito);
        notificationService.inviaInvito(invito);
    }

    // TODO?
    public void creaInvitoTeam(MembroTeam mittente, Utente destinatario) {}


    public void rispostaInvito(Invito invito, boolean accetta) {
        switch (invito) {
            case InvitoHackathon staff -> gestisciRispostaStaff(staff, accetta);
            case InvitoTeam team -> gestisciRispostaTeam(team, accetta);
            default -> throw new IllegalStateException("Tipo di invito non riconosciuto: " + invito);
        }
        invitoService.elimina(invito);
    }

    //TODO accetta e rifiuta invito con relativa implementazione dei metodi
    private void gestisciRispostaTeam(InvitoTeam team, boolean accetta) {
        if (accetta) {
            if (team.getDestinatario().getTeam() != null) {
                throw new UnicoTeamException();
            }

        }
    }


//  Il caso d'uso Risposta membro staff viene attivato dall'utente invitato come membro di staff (ha risposto all'invito).
//  La risposta viene gestita diversamente in base al ruolo per cui l'utente era stato invitato, se come giudice o come mentore.
    private void gestisciRispostaStaff(InvitoHackathon staff, boolean accetta) {
        RuoliStaff ruolo = staff.getRuolo();
        Hackathon hackathon = staff.getHackathon();

        switch (ruolo) {
            case GIUDICE -> gestisciRispostaGiudice(staff, accetta);
            case MENTORE -> gestisciRispostaMentore(staff, accetta);
        }

        if (hackathon.getStato() == BOZZA) {
            int numGiudici = Math.toIntExact(hackathon.getRuoli().stream()
                    .filter(rl -> rl.getTipoRuolo() == RuoliStaff.GIUDICE)
                    .count());

            int numMentori = Math.toIntExact(hackathon.getRuoli().stream()
                    .filter(rl -> rl.getTipoRuolo() == RuoliStaff.MENTORE)
                    .count());

            if (numGiudici == 1 & numMentori >= 1) {
                hackathon.cambiaStato(new ConfermatoState(hackathon));
                notificationService.inviaNotificaHackathonConfermato(hackathon);
            }
        }
    }


    private void gestisciRispostaGiudice(InvitoHackathon staff, boolean accetta) {
        Hackathon hack = staff.getHackathon();
        // l'utente ha accettato di diventare giudice: viene aggiunto allo staff dell'hackathon
        if (accetta) {
            roleFactory.creaERegistraRuolo(staff.getRuolo(), staff.getDestinatario(), hack);
            notificationService.inviaNotificaInvitoAccettato(staff);
            // l'utente non ha accettato di diventare giudice: lo staff di quell'hackathon è dichiarato incompleto
        } else {
            HackHandler.setStaffIncompleto(hack, INCOMPLETO);
            notificationService.inviaNotificaStaffIncompleto(hack, (List<InvitoHackathon>) staff);
        }
    }


    private void gestisciRispostaMentore(InvitoHackathon staff, boolean accetta) {
        Hackathon hack = staff.getHackathon();
        if (accetta) {
            roleFactory.creaERegistraRuolo(staff.getRuolo(), staff.getDestinatario(), hack);
            notificationService.inviaNotificaInvitoAccettato(staff);
        } else {
            if (hack.getStato() == BOZZA) {
                int numMentori = Math.toIntExact(hack.getRuoli().stream()
                        .filter(rp -> rp.getTipoRuolo() == RuoliStaff.MENTORE)
                        .count());

                if (numMentori == 0) {
                    // sottraggo l'invito del mentore che ha appena risposto
                    int numInvitiMentori = this.getInvitiMentori(hack) - 1;

                    if (numInvitiMentori == 0) {
                        HackHandler.setStaffIncompleto(hack, INCOMPLETO);
                        notificationService.inviaNotificaStaffIncompleto(hack, (List<InvitoHackathon>) staff);
                    }
                }
            }

            if (hack.getStaffIncompleto() != INCOMPLETO)  notificationService.inviaNotificaInvitoRifiutato(staff);
        }
    }


    /**
     * Metodo che verifica se la scadenza dell'invito per un membro dello staff è scaduto.
     * Crea una lista degli inviti scaduti e li elimina, mandando una notifica all'organizzatore.
     * Qualora l'hackathon per cui era stato mandato l'invito sia ancora in stato di BOZZA, avvisa l'organizzatore.
     */
    public void verificaScadenze() {
        List<InvitoHackathon> scaduti = invitoService.getInviti().stream()
                .filter(InvitoHackathon.class::isInstance)
                .map(InvitoHackathon.class::cast)
                .filter(i -> i.getScadenza().isBefore(LocalDateTime.now()))
                .toList();

        Map<Hackathon, List<InvitoHackathon>> perHackathon =
                scaduti.stream().collect(Collectors.groupingBy(InvitoHackathon::getHackathon));

        for (Map.Entry<Hackathon, List<InvitoHackathon>> entry : perHackathon.entrySet()) {
            Hackathon hack = entry.getKey();
            List<InvitoHackathon> invitiScaduti = entry.getValue();


            if (hack.getStato() == BOZZA) {
                HackHandler.setStaffIncompleto(hack, INCOMPLETO);
                notificationService.inviaNotificaStaffIncompleto(hack, invitiScaduti);
            }

            invitiScaduti.forEach(notificationService::inviaNotificaInvitoScaduto);
            invitiScaduti.forEach(invitoService::elimina);
        }
    }

    private int getInvitiMentori(Hackathon hack) {
        return Math.toIntExact(invitoService.getInviti().stream()
                .filter(inv -> inv instanceof InvitoHackathon)
                .map(inv -> (InvitoHackathon) inv)
                .filter(inv -> inv.getHackathon().equals(hack))
                .filter(inv -> inv.getRuolo() == RuoliStaff.MENTORE)
                .count());
    }
}
