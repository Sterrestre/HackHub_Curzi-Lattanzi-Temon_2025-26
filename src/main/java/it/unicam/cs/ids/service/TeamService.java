package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.repository.HackathonRepository;
import it.unicam.cs.ids.repository.TeamRepository;
import it.unicam.cs.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UtenteRepository utenteRepository;
    private final HackathonRepository hackathonRepository;

    public TeamService(TeamRepository teamRepository, UtenteRepository utenteRepository,
                       HackathonRepository hackathonRepository) {
        this.teamRepository = teamRepository;
        this.utenteRepository = utenteRepository;
        this.hackathonRepository = hackathonRepository;
    }

    public Team creaTeam(String nome, Utente amministratore) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome team obbligatorio");
        }
        if (teamRepository.existsByNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Esiste già un team con questo nome");
        }
        if (amministratore.getTeam() != null) {
            throw new IllegalStateException("L'utente è già membro di un team");
        }
        Team team = new Team(nome);
        teamRepository.save(team);
        MembroTeam nuovoMembro = team.aggiungiMembro(amministratore);
        team.rendiAmministratore(nuovoMembro);
        utenteRepository.save(amministratore);
        teamRepository.save(team);
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
        if (utente.getTeam() != null) {
            throw new IllegalStateException("L'utente è già membro di un team");
        }
        MembroTeam nuovoMembro = team.aggiungiMembro(utente);
        if (amministratore) {
            team.rendiAmministratore(nuovoMembro);
        }
        teamRepository.save(team);
        return nuovoMembro;
    }

    public void rimuoviMembro(Team team, MembroTeam membro) {
        team.rimuoviMembro(membro);
        teamRepository.save(team);
    }
}