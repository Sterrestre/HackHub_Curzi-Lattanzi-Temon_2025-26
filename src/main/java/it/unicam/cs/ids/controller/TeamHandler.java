package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.NotificationService;
import it.unicam.cs.ids.service.team.TeamService;

import java.time.LocalDateTime;
import java.util.List;

public class TeamHandler {

    private final TeamService teamService;
    private final NotificationService notificationService;

    public TeamHandler(TeamService teamService, NotificationService notificationService) {
        this.teamService = teamService;
        this.notificationService = notificationService;
    }

    public Team creaTeam(String nome, Utente amministratore) {
        // Validazioni di input
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome team obbligatorio");
        }

        Team team = teamService.creaTeam(nome, amministratore);
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

    public void rendiAmministratore(MembroTeam membroTeam) {}

    public TeamIscritto iscriviTeam(Team team, Hackathon hackathon, MembroTeam amministratore) {
        LocalDateTime scadIscr = hackathon.getInfoHack().getScadenzaIscrizioni();
        if (scadIscr.isBefore(LocalDateTime.now())) {
            throw new Scaduto();
        }
        int numMax = hackathon.getInfoHack().getNumMaxTeam();

        if (numMax < hackathon.getTeamIscritti().stream().count()) {
            throw new HackCompleto();
        }

        List<MembroTeam> membroTeamRuolo = team.getMembri().stream().map().toList();
        return null;
    }

    public void iscriviMembroTeam(MembroTeam membTeam, Hackathon hackathon) {
    }
}