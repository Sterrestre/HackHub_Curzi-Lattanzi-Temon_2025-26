package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.RichiestaSupporto;

public record RichiestaSupportoDTO(
        String richiestaId,
        String dettagli,
        boolean risolta
) {
    public static RichiestaSupportoDTO from(RichiestaSupporto richiesta) {
        return new RichiestaSupportoDTO(
                richiesta.getRichiestaSuppID(),
                richiesta.getDettagli(),
                richiesta.isVisualizzata()
        );
    }
}

