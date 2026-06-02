package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.RichiestaSupporto;

import java.util.Map;

public record RichiestaSupportoDTO(
        String richiestaId,
        String dettagli,
        boolean risolta
) {
    public static RichiestaSupportoDTO from(Map.Entry<RichiestaSupporto, Boolean> entry) {
        return new RichiestaSupportoDTO(
                entry.getKey().getRichiestaSuppID(),
                entry.getKey().getDettagli(),
                entry.getValue()
        );
    }
}

