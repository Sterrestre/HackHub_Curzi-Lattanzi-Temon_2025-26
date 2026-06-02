package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.hackathon.InfoHack;

import java.time.LocalDateTime;

public record InfoHackDTO(
        String regolamento,
        LocalDateTime dataInizio,
        LocalDateTime dataFine,
        LocalDateTime scadenzaIscrizioni,
        String luogo,
        double quotaIscrizione,
        double premio,
        int numMaxTeam,
        int maxPartecipantiPerTeam
) {
    public static InfoHackDTO from(InfoHack info) {
        return new InfoHackDTO(
                info.getRegolamento(),
                info.getDataInizio(),
                info.getDataFine(),
                info.getScadenzaIscrizioni(),
                info.getLuogo(),
                info.getQuotaIscrizione(),
                info.getPremio(),
                info.getNumMaxTeam(),
                info.getDimMaxTeam()
        );
    }
}

