package it.unicam.cs.ids.service;

import it.unicam.cs.ids.handler.TeamHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.repository.HackathonRepository;
import it.unicam.cs.ids.repository.TeamRepository;
import it.unicam.cs.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;

/**
 * Service per la gestione dei team e dei loro membri.
 * Si occupa di recuperare le entità dal repository e delegare
 * la logica di dominio agli handler.
 */
@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;
    private final HackathonRepository hackathonRepository;
    private final TeamHandler teamHandler;

    public TeamService(TeamRepository teamRepository, UtenteRepository utenteRepository, HackathonRepository hackathonRepository, TeamHandler teamHandler) {
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
        this.hackathonRepository = hackathonRepository;
        this.teamHandler = teamHandler;
    }

    public Team creaTeam(String nome, Utente amministratore) {
        Team team = teamHandler.creaTeam(nome); // valida i dati e lancia eccezioni se non validi
        teamRepository.save(team); // salva il team prima
        teamHandler.aggiungiMembroTeam(team, amministratore, true); // aggiunge l'amministratore come membro del team
        utenteRepository.save(amministratore); // salva l'utente aggiornato
        teamRepository.save(team); // salva il team con il membro
        return team;
    }

    public Team findTeamById(String teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
    }

    public TeamIscritto findTeamIscrittoById(String teamIscrittoId) {
        return hackathonRepository.findTeamIscrittoById(teamIscrittoId)
                .orElseThrow(() -> new IllegalArgumentException("TeamIscritto non trovato"));
    }

    public MembroTeam findMembroTeamById(String teamId, String membroId) {
        Team team = findTeamById(teamId);
        return team.getMembri().stream()
                .filter(m -> m.getMembroTeamId().equals(membroId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("MembroTeam non trovato nel team " + team.getNome()));
    }

    public MembroTeam aggiungiMembro(Team team, Utente utente, boolean amministratore) {
        MembroTeam nuovoMembro = teamHandler.aggiungiMembroTeam(team, utente, amministratore);
        teamRepository.save(team);
        return nuovoMembro;
    }

    public void rimuoviMembro(Team team, MembroTeam membro) {
        team.rimuoviMembro(membro);
        teamRepository.save(team);
    }
}
