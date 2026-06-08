package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.MetodoPagamento;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.MembroTeamIscritto;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.NotificationFacade;
import it.unicam.cs.ids.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MembriTeamHandler {

    private final TeamService teamService;
    private final NotificationFacade notificationFacade;

    public MembriTeamHandler (TeamService teamService, NotificationFacade notificationFacade) {
        this.teamService = teamService;
        this.notificationFacade = notificationFacade;
    }


    /**
     * Rende un membro del team amministratore. Solo un amministratore può rendere un altro membro amministratore.
     * @param admin
     * @param membroTeam
     * @throws IllegalArgumentException se l'utente che vuole rendere amministratore un altro membro non è amministratore o se il membro da rendere amministratore non appartiene allo stesso team dell'amministratore o se il membro da rendere amministratore è già amministratore
     * @return un messaggio di successo
     */
    // Implementazione CU "Rendi amministratore" - it 3
    public MembroTeam rendiAmministratore(MembroTeam admin, MembroTeam membroTeam) {
        // Controllo del prerequisitoo: solo un amministratore può rendere un altro membro amministratore
        if (!admin.isAmministratore()) {
            throw new IllegalArgumentException("Solo un amministratore può rendere un altro membro amministratore.");
        }

        if (membroTeam.getTeam() == admin.getTeam()) {
            if (membroTeam.isAmministratore()) {
                throw new IllegalArgumentException(membroTeam.getUtente().getNickname() + " è già amministratore del team.");
            } else {
                membroTeam.setAmministratore(true);
                notificationFacade.inviaNotificaAmministratore(membroTeam);
                return membroTeam;
            }
        } else {
            throw new IllegalArgumentException("Il membro da rendere amministratore deve appartenere allo stesso team dell'amministratore.");
        }
    }


    /**
     * Rimuove un membro del team dal team di cui fa attualmente parte
     * @param membroTeam
     */
    public void rimuoviMembroTeam(MembroTeam membroTeam) {
        Utente utente = membroTeam.getUtente();
        List<Hackathon> partecipazioni = new ArrayList<>(utente.getPartecipazioni());
        for (Hackathon hack : partecipazioni) {
            // Se l'hackathon non è concluso, trovo il teamIscritto corrispondente al membro del team e
            // rimuovo il membro del team --> CU "Disiscriviti"
            if (hack.getStato() != Stato.CONCLUSO) {
                hack.getTeamIscritti().stream()
                        .filter(ti -> ti.getElencoIscritti().stream()
                                .anyMatch(mi -> mi.getUtente().equals(utente)))
                        .findFirst()
                        .ifPresent(teamIscritto -> {
                            teamIscritto.getElencoIscritti().removeIf(mi -> mi.getUtente().equals(membroTeam.getUtente()));
                        });

                utente.getPartecipazioni().remove(hack);
            }
        }

        Team team = membroTeam.getTeam();
        utente.setTeam(null);
        teamService.rimuoviMembro(team, membroTeam);
    }


    public MembroTeamIscritto iscriviMembroTeam(MembroTeam membTeam, Hackathon hackathon) {
        // Controllo che il team sia già iscritto all'hackathon
        TeamIscritto teamIscr = hackathon.getTeamIscritti().stream()
                .filter(t -> t.getTeam().equals(membTeam.getTeam()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Il team non è iscritto all'hackathon"));

        // Controllo che ci siano ancora posti per i membri del team
        int membriIscritti = teamIscr.getElencoIscritti().size();
        if (membriIscritti >= hackathon.getInfoHack().getDimMaxTeam()) {
            throw new IllegalArgumentException("Il team ha raggiunto il numero massimo di membri");
        }

        MetodoPagamento metPag = MetodoPagamento.NON_SELEZIONATO;
        if (hackathon.getPremioInDenaro() > 0) {
            // TODO FUTURO chiedere il metodo di pagamento all'attore
        }

        MembroTeamIscritto membroTeamIscritto = new MembroTeamIscritto(membTeam.getUtente(), teamIscr, metPag);
        teamIscr.getElencoIscritti().add(membroTeamIscritto);
        membTeam.getUtente().getPartecipazioni().add(hackathon);

        return membroTeamIscritto;
    }
}
