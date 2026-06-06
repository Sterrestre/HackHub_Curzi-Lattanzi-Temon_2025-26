package it.unicam.cs.ids.dto;

public record ApplicaPenalizzazioneRequest(
        String teamId,
        String tipoIntervento,
        String motivazione
) {}