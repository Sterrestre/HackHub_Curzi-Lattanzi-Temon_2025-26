package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.hackathon.Hackathon;

import java.util.List;

public record StaffHackathonDTO(
        String organizzatoreNickname,
        String giudiceNickname,
        List<String> mentoriNickname
) {
    public static StaffHackathonDTO from(Hackathon h) {
        String giudice = h.getRuoli().stream()
                .filter(r -> r.getTipoRuolo().name().equals("GIUDICE"))
                .map(r -> r.getUtente().getNickname())
                .findFirst()
                .orElse(null);

        List<String> mentori = h.getRuoli().stream()
                .filter(r -> r.getTipoRuolo().name().equals("MENTORE"))
                .map(r -> r.getUtente().getNickname())
                .toList();

        return new StaffHackathonDTO(
                h.getOrganizzatore().getNickname(),
                giudice,
                mentori
        );
    }
}