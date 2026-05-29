package it.unicam.cs.ids.dto;

import java.time.LocalDateTime;

public record ModificaHackathonRequest(
        String descrizione,
        LocalDateTime dataInizio,
        LocalDateTime dataFine,
        LocalDateTime scadenzaIscrizioni,
        String luogo,
        Double quotaIscrizione,
        Double premio,
        Integer numMaxTeam,
        Integer maxPartecipantiPerTeam
) {}

