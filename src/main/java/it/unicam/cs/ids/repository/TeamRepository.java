package it.unicam.cs.ids.repository;

import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, String> {

    // Query opzionali utili (solo se servono)
    List<Team> findByNomeContainingIgnoreCase(String nome);

    // TODO: i teamIscritti si recuperano dall'Hackathon dice copilot... controlla
    List<TeamIscritto> findTeamIscrittoByTeamId(String teamId);
}

