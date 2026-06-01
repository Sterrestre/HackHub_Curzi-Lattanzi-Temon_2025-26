package it.unicam.cs.ids.dto;

public record ValutaSottomissioneRequest(
        String sottomissioneId,
        double voto,
        String giudizio
) {}

