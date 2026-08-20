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
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.repository.InvitoRepository;
import it.unicam.cs.ids.service.NotificationFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static it.unicam.cs.ids.model.hackathon.StaffIncompleto.INCOMPLETO;
import static it.unicam.cs.ids.model.hackathon.Stato.BOZZA;

/**
 * Handler per la logica di dominio degli inviti.
 * Gestisce creazione, invio e risposta agli inviti sia per lo staff che per i team.
 */
@Service
public class InvitiHandler {

    private final InvitoRepository invitoRepository;
    private final NotificationFacade notificationFacade;
    private final RoleFactory roleFactory;

    /** Giorni di validità di un invito staff, letti da application.properties. */
    @Value("${invito.scadenza.giorni}")
    private int giorniScadenzaInvito;

    public InvitiHandler(InvitoRepository invitoRepository,
                         NotificationFacade notificationFacade, RoleFactory roleFactory) {
        this.invitoRepository = invitoRepository;
        this.notificationFacade = notificationFacade;
        this.roleFactory = roleFactory;
    }

    /**
     * Crea e invia un invito a un utente per ricoprire un ruolo di staff in un hackathon.
     */
    public void creaInvitoStaff(Utente organizzatore, Utente destinatario,
                                Hackathon hackathon, RuoliStaff ruolo) {
        LocalDateTime scadenza = LocalDateTime.now().plusDays(giorniScadenzaInvito);
        InvitoHackathon invito = new InvitoHackathon(organizzatore, destinatario, hackathon, ruolo, scadenza);
        notificationFacade.inviaInvito(invito);
    }

    /**
     * Crea e invia un invito a un utente per unirsi a un team.
     */
    public void creaInvitoTeam(MembroTeam mittente, Utente destinatario, Team team) {
        InvitoTeam invito = new InvitoTeam(mittente, destinatario, team);
        notificationFacade.inviaInvito(invito);
    }

    /**
     * Gestisce la risposta a un invito, distinguendo tra invito staff e invito team.
     * Dopo aver processato la risposta, elimina l'invito.
     */
    public void rispostaInvito(Invito invito, boolean accetta) {
        switch (invito) {
            case InvitoHackathon staff -> gestisciRispostaStaff(staff, accetta);
            case InvitoTeam team -> gestisciRispostaTeam(team, accetta);
            default -> throw new IllegalStateException("Tipo di invito non riconosciuto: " + invito);
        }
    }

    /**
     * Gestisce la risposta a un invito team.
     * Se accettato, aggiunge il destinatario al team (solo se non appartiene già a un team).
     */
    private void gestisciRispostaTeam(InvitoTeam invito, boolean accetta) {
        if (accetta) {
            if (invito.getDestinatario().getTeam() != null) {
                throw new UnicoTeamException();
            }
            invito.getTeam().aggiungiMembro(invito.getDestinatario());
        }
    }

    /**
     * Gestisce la risposta a un invito staff (giudice o mentore).
     * Se dopo la risposta lo staff è completo, l'hackathon passa allo stato CONFERMATO.
     */
    private void gestisciRispostaStaff(InvitoHackathon staff, boolean accetta) {
        RuoliStaff ruolo = staff.getRuolo();
        Hackathon hackathon = staff.getHackathon();

        if (accetta) {
            boolean giaHaAltroRuolo = hackathon.getRuoli().stream()
                    .anyMatch(r -> r.getUtente().getUtenteID().equals(staff.getDestinatario().getUtenteID())
                            && r.getTipoRuolo() != ruolo);
            if (giaHaAltroRuolo) {
                throw new IllegalStateException("Questo utente ricopre già un altro ruolo di staff per questo hackathon");
            }
        }

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

            if (numGiudici == 1 && numMentori >= 1) {
                hackathon.cambiaStato(new ConfermatoState(hackathon));
                notificationFacade.inviaNotificaHackathonConfermato(hackathon);
            }
        }
    }

    /**
     * Gestisce la risposta di un giudice a un invito staff.
     * Se rifiuta, lo staff dell'hackathon viene dichiarato incompleto.
     */
    private void gestisciRispostaGiudice(InvitoHackathon staff, boolean accetta) {
        Hackathon hack = staff.getHackathon();
        if (accetta) {
            roleFactory.creaERegistraRuolo(staff.getRuolo(), staff.getDestinatario(), hack);
            staff.getDestinatario().setMembroDiStaff(true);
            notificationFacade.inviaNotificaInvitoAccettato(staff);
        } else {
            HackHandler.setStaffIncompleto(hack, INCOMPLETO);
            notificationFacade.inviaNotificaStaffIncompleto(hack, List.of(staff));
        }
    }

    /**
     * Gestisce la risposta di un mentore a un invito staff.
     * Se rifiuta e non ci sono altri mentori né inviti pendenti, lo staff è incompleto.
     */
    private void gestisciRispostaMentore(InvitoHackathon staff, boolean accetta) {
        Hackathon hack = staff.getHackathon();
        if (accetta) {
            roleFactory.creaERegistraRuolo(staff.getRuolo(), staff.getDestinatario(), hack);
            staff.getDestinatario().setMembroDiStaff(true);
            notificationFacade.inviaNotificaInvitoAccettato(staff);
        } else {
            if (hack.getStato() == BOZZA) {
                int numMentori = Math.toIntExact(hack.getRuoli().stream()
                        .filter(rp -> rp.getTipoRuolo() == RuoliStaff.MENTORE)
                        .count());

                if (numMentori == 0) {
                    int numInvitiMentori = this.getInvitiMentori(hack) - 1;
                    if (numInvitiMentori == 0) {
                        HackHandler.setStaffIncompleto(hack, INCOMPLETO);
                        notificationFacade.inviaNotificaStaffIncompleto(hack, List.of(staff));
                    }
                }
            }
            if (hack.getStaffIncompleto() != INCOMPLETO) {
                notificationFacade.inviaNotificaInvitoRifiutato(staff);
            }
        }
    }

    /**
     * Verifica le scadenze degli inviti staff e gestisce quelli scaduti.
     * Notifica l'organizzatore se l'hackathon è ancora in bozza.
     */
    public void verificaScadenze() {
        List<InvitoHackathon> scaduti = invitoRepository.findAll().stream()
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
                notificationFacade.inviaNotificaStaffIncompleto(hack, invitiScaduti);
            }

            invitiScaduti.forEach(notificationFacade::inviaNotificaInvitoScaduto);
        }
    }

    /** Conta gli inviti pendenti per ruolo MENTORE relativi a un hackathon specifico. */
    private int getInvitiMentori(Hackathon hack) {
        return Math.toIntExact(invitoRepository.findAll().stream()
                .filter(inv -> inv instanceof InvitoHackathon)
                .map(inv -> (InvitoHackathon) inv)
                .filter(inv -> inv.getHackathon().equals(hack))
                .filter(inv -> inv.getRuolo() == RuoliStaff.MENTORE)
                .count());
    }
}