package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.team.MembroTeam;

public record MembroTeamDTO(
        String id,
        String nickname,
        boolean amministratore
) {
    public static MembroTeamDTO from(MembroTeam membro) {
        return new MembroTeamDTO(
                membro.getMembroTeamId(),
                membro.getUtente().getNickname(),
                membro.isAmministratore()
        );
    }
}

