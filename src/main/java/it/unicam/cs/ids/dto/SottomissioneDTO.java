package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.Sottomissione;

public record SottomissioneDTO(
        String id,
        String titolo,
        String descrizione,
        Object linkRepository,
        boolean valutata,
        Integer voto,
        String giudizio
) {


    // TODO revisionare e correggere le chiamate ai metodi
    public static SottomissioneDTO from(Sottomissione s) {
        return new SottomissioneDTO(
                s.getId(),
                s.titolo(),
                s.descrizione(),
                s.linkRepository(),
                s.isValutata(),
                s.isValutata() ? s.getValutazione().getVoto() : null,
                s.isValutata() ? s.getValutazione().getGiudizio() : null
        );
    }
}

