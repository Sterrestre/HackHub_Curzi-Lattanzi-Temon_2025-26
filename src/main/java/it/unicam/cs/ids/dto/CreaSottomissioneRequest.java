package it.unicam.cs.ids.dto;

public record CreaSottomissioneRequest(
        String teamId,
        String hackathonId,
        String titolo,
        String descrizione,
        String linkRepository
) {}

