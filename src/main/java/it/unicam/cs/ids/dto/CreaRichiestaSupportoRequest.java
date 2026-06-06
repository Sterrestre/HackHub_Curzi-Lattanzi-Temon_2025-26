package it.unicam.cs.ids.dto;

public record CreaRichiestaSupportoRequest(
        String hackathonId,
        String teamId,
        String dettagli
) {}