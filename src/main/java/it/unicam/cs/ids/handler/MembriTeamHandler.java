package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.MetodoPagamento;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.MembroTeamIscritto;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.team.TeamService;
import org.springframework.stereotype.Service;

@Service
public class MembriTeamHandler {

    private final TeamService teamService;

    public MembriTeamHandler (TeamService teamService) {
        this.teamService = teamService;
    }


    /**
     * Rende un membro del team amministratore. Solo un amministratore può rendere un altro membro amministratore.
     * @param admin
     * @param membroTeam
     * @throws IllegalArgumentException se l'utente che vuole rendere amministratore un altro membro non è amministratore o se il membro da rendere amministratore non appartiene allo stesso team dell'amministratore o se il membro da rendere amministratore è già amministratore
     * @return un messaggio di successo
     */
    public String rendiAmministratore(MembroTeam admin, MembroTeam membroTeam) {
        if (!admin.isAmministratore()) {
            throw new IllegalArgumentException("Solo un amministratore può rendere un altro membro amministratore.");
        }

        if (membroTeam.getTeam() == admin.getTeam()) {
            if (membroTeam.isAmministratore()) {
                throw new IllegalArgumentException("Il membro è già amministratore");
            }
            membroTeam.setAmministratore(true);
            // TODO invia notifica
            // notificationService.inviaNotificaAmministratore();
        }
        return "Il membro del team "+membroTeam.getUtente().getNickname()+" è ora amministratore";
    }


    /**
     * Rimuove un membro del team dal team di cui fa attualmente parte
     * @param membroTeam
     */
    public void rimuoviMembroTeam(MembroTeam membroTeam) {
        Team team = membroTeam.getTeam();
        membroTeam.getUtente().setTeam(null);
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
        return membroTeamIscritto;
    }
}
