package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.inviti.InvitoHackathon;

public record InvitoStaffDTO(
        String id,
        String hackathonId,
        String nomeHackathon,
        String ruolo,
        String mittenteNickname
) {
    public static InvitoStaffDTO from(InvitoHackathon i) {
        return new InvitoStaffDTO(
                i.getId(),
                i.getHackathon().getHackathonID(),
                i.getHackathon().getNome(),
                i.getRuolo().name(),
                i.getMittente().getNickname()
        );
    }
}