package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.MetodoPagamento;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.MembroTeamIscritto;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.NotificationService;
import it.unicam.cs.ids.service.team.TeamService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeamHandler {

    private final TeamService teamService;
    private final NotificationService notificationService;

    public TeamHandler(TeamService teamService, NotificationService notificationService) {
        this.teamService = teamService;
        this.notificationService = notificationService;
    }

    public Team creaTeam(String nome, Utente amministratore) {
        //TODO implementare il catch dell'eccezione che implementi l'opt team != null
        if (amministratore.getTeam() != null) {
            throw new IllegalStateException("L'utente è già membro di un team");
        };

        //TODO form con i dati da inserire
        //Validazioni di input
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome team obbligatorio");
        }

        Team team = teamService.creaTeam(nome, amministratore);
        // aggiungi membro team? TeamHandler o MembroTeamHandler?
        MembroTeam admin = new MembroTeam(amministratore, team, true);
    // TODO
        //  notificationService.inviaNotificaCreazioneTeam(team, amministratore);

        return team;
    }

    //TODO
    public void aggiungiMembroTeam(Team team, Utente utente) {
        MembroTeam nuovo = teamService.aggiungiMembro(team, utente);
    //    notificationService.inviaNotificaAggiuntaMemebro(?);
    }

    public void rimuoviMembroTeam(MembroTeam membroTeam) {}

    public String rendiAmministratore(MembroTeam admin, MembroTeam membroTeam) {
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

    public TeamIscritto iscriviTeam(Team team, Hackathon hackathon, MembroTeam amministratore) {
        LocalDateTime scadIscr = hackathon.getInfoHack().getScadenzaIscrizioni();
        if (scadIscr.isBefore(LocalDateTime.now())) {
            throw new Scaduto();
        }
        int numMax = hackathon.getInfoHack().getNumMaxTeam();

        if (numMax < hackathon.getTeamIscritti().stream().count()) {
            throw new HackCompleto();
        }

        List<MembroTeam> membroTeamRuolo = team.getMembri().stream().toList();
        return null;
    }

    public MembroTeamIscritto iscriviMembroTeam(MembroTeam membTeam, Hackathon hackathon) {
        TeamIscritto teamIscr = hackathon.getTeamIscritti().stream()
                .filter(t -> t.getTeam().equals(membTeam.getTeam()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Il team non è iscritto all'hackathon"));

        MetodoPagamento metPag = MetodoPagamento.NON_SELEZIONATO;
        if (hackathon.getPremioInDenaro() > 0) {
            // TODO chiedere il metodo di pagamento all'attore
        }

        MembroTeamIscritto membroTeamIscritto = new MembroTeamIscritto(membTeam.getUtente(), teamIscr, metPag);
        teamIscr.getElencoIscritti().add(membroTeamIscritto);
        return membroTeamIscritto;
    }
}