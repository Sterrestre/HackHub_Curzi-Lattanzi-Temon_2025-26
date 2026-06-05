package it.unicam.cs.ids.dto;

import java.time.LocalDateTime;

public record CreaHackathonRequest(
        String organizzatoreId,
        String nome,
        String regolamento,
        LocalDateTime dataInizio,
        LocalDateTime dataFine,
        LocalDateTime scadenzaIscrizioni,
        String luogo,
        double quotaIscrizione,
        double premio,
        int numMaxTeam,
        int maxPartecipantiPerTeam
) {}

