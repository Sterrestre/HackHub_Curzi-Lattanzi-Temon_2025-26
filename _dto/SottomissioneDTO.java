package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.Sottomissione;

public record SottomissioneDTO(
        Long id,
        String titolo,
        String descrizione,
        String linkRepository,
        boolean valutata,
        Double voto,
        String giudizio
) {

    // TODO revisionare e correggere le chiamate ai metodi
    public static SottomissioneDTO from(Sottomissione s) {
        return new SottomissioneDTO(
                s.getId(),
                s.getTitolo(),
                s.getDescrizione(),
                s.getLinkRepository(),
                s.isValutata(),
                s.isValutata() ? s.getValutazione().getVoto() : null,
                s.isValutata() ? s.getValutazione().getGiudizio() : null
        );
    }
}

