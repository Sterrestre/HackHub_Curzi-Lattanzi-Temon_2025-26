package it.unicam.cs.ids.dto;

public record CreaSottomissioneRequest(
        String teamIscrittoId,
        String hackathonId,
        String titolo,
        String descrizione,
        String linkRepository
) {}

