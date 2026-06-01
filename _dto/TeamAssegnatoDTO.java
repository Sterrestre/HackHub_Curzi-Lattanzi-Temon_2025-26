package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.team.TeamIscritto;

public record TeamAssegnatoDTO(
        String teamId,
        String nomeTeam
) {
    public static TeamAssegnatoDTO from(TeamIscritto t) {
        return new TeamAssegnatoDTO(
                t.getTeam().getTeamID(),
                t.getTeam().getNome()
        );
    }
}

