package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.repository.TeamRepository;
import it.unicam.cs.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;

    public TeamService(TeamRepository teamRepository, UtenteRepository utenteRepository) {
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
    }

    public Team creaTeam(String nome, Utente amministratore) {
        Team team = new Team(nome);
        MembroTeam creatore = team.aggiungiMembro(amministratore);
        team.rendiAmministratore(creatore);
        teamRepository.save(team);
        return team;
    }

    public Team findTeamById(String teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
    }

    public TeamIscritto findTeamIscrittoById(String teamIscrittoId) {
        // TODO rivedi: voglio un TeamIscritto, non una lista di TeamIscritto, e non deve essere null
        return teamRepository.findTeamIscrittoByTeamId(teamIscrittoId)
                .stream()
                .filter(ti -> ti.getTeamIscrittoId().equals(teamIscrittoId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("TeamIscritto non trovato"));
    }

    public MembroTeam findMembroTeamById(String teamId, String membroId) {
        Team team = findTeamById(teamId);

        return team.getMembri().stream()
                .filter(m -> m.getMembroTeamId().equals(membroId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("MembroTeam non trovato nel team" + team.getNome()));
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

}
