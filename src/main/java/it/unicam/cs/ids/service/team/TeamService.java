package it.unicam.cs.ids.service.team;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TeamService {

    private final java.util.Map<String, Team> teamMap = new java.util.HashMap<>();

    public Team creaTeam(String nome, Utente amministratore) {
        // TODO il costruttore vuole uno String ID prima del nome
        Team team = new Team(nome);
        MembroTeam creatore = team.aggiungiMembro(amministratore);
        team.rendiAmministratore(creatore);
        teamMap.put(team.getTeamID(), team);
        return team;
    }

    public Team findById(String teamId) {
        if (teamId == null || teamId.isBlank()) {
            throw new IllegalArgumentException("Team ID non può essere nullo o vuoto");
        }
        Team team = teamMap.get(teamId);
        if (team == null) {
            throw new IllegalArgumentException("Team non trovato con ID: " + teamId);
        }
        return team;
    }

    public MembroTeam aggiungiMembro(Team team, Utente utente, boolean amministratore) {
        MembroTeam nuovoMembro = team.aggiungiMembro(utente);
        if (amministratore) {
            team.rendiAmministratore(nuovoMembro);
        }
        return  nuovoMembro;
    }

    public void rimuoviMembro(Team team, MembroTeam membro) {
        team.rimuoviMembro(membro);
    }

    public void rendiAmministratore(Team team, MembroTeam membro) {
        team.rendiAmministratore(membro);
    }


    // COMPETENZA DEL DB?
    private final List<MembroTeam> membriTeam = new ArrayList<>();

    public void salva(MembroTeam  membroTeam) {
        membriTeam.add(membroTeam);
    }

    public void lasciaTeam(MembroTeam  membroTeam) {
        membriTeam.remove(membroTeam);
    }

    public List<MembroTeam> getMemebriTeam() {
        return Collections.unmodifiableList(membriTeam);
    }
}
