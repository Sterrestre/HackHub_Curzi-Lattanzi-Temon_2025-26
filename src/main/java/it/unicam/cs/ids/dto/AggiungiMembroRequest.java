package it.unicam.cs.ids.dto;

public record AggiungiMembroRequest(
        String teamId,
        String utenteId,
        boolean amministratore
) {}