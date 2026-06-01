package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.team.Team;

import java.util.List;

public record TeamDTO(
        String id,
        String nome,
        List<MembroTeamDTO> membri
) {
    public static TeamDTO from(Team team) {
        return new TeamDTO(
                team.getTeamID(),
                team.getNome(),
                team.getMembri().stream()
                        .map(MembroTeamDTO::from)
                        .toList()
        );
    }
}

