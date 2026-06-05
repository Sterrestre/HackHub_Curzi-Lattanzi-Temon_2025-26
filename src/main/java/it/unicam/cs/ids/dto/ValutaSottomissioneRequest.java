package it.unicam.cs.ids.dto;

public record ValutaSottomissioneRequest(
        String giudiceId,
        String hackathonId,
        String teamIscrittoId,
        double voto,
        String giudizio
) {}

