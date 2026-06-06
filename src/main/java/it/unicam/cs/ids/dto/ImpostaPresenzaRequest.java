package it.unicam.cs.ids.dto;

public record ImpostaPresenzaRequest(
        String teamId,
        String utenteId,
        boolean presente
) {}