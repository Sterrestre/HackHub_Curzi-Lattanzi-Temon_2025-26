package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.Stato;


public record HackathonDTO(
        String id,
        String nome,
        InfoHackDTO info,
        int numTeamIscritti,
        Stato stato
) {
    public static HackathonDTO from(Hackathon h) {
        return new HackathonDTO(
                h.getHackathonID(),
                h.getNome(),
                InfoHackDTO.from(h.getInfoHack()),
                h.getTeamIscritti().size(),
                h.getStato()
        );
    }
}


