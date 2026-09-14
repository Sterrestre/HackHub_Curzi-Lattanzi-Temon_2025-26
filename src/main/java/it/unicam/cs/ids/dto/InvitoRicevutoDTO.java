package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.inviti.Invito;
import it.unicam.cs.ids.model.inviti.InvitoHackathon;
import it.unicam.cs.ids.model.inviti.InvitoTeam;

public record InvitoRicevutoDTO(
        String id,
        String tipo,        // "STAFF" oppure "TEAM"
        String titolo,      // nome hackathon (staff) o nome team (team)
        String dettaglio     // ruolo (GIUDICE/MENTORE) per staff, nickname di chi invita per team
) {
    public static InvitoRicevutoDTO from(Invito i) {
        if (i instanceof InvitoHackathon staff) {
            return new InvitoRicevutoDTO(
                    staff.getId(),
                    "STAFF",
                    staff.getHackathon().getNome(),
                    staff.getRuolo().name()
            );
        }
        if (i instanceof InvitoTeam team) {
            return new InvitoRicevutoDTO(
                    team.getId(),
                    "TEAM",
                    team.getTeam().getNome(),
                    team.getMittente().getUtente().getNickname()
            );
        }
        throw new IllegalStateException("Tipo di invito non riconosciuto");
    }
}