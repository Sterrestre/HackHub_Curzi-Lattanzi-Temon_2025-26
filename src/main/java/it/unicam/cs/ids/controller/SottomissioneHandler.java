package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.StatoSottomissione;
import it.unicam.cs.ids.model.TeamIscritto;
import it.unicam.cs.ids.model.Valutazione;

import java.io.File;
import java.time.LocalDateTime;

public class SottomissioneHandler {


    public void caricaSottomissione(TeamIscritto teamIscritto, File sottomissioneFile) {
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto nullo");
        }
        if (sottomissioneFile == null) {
            throw new IllegalArgumentException("File sottomissione nullo");
        }

        // Nel flow/sequence: se non esiste -> crea, se esiste -> sostituisce
        Sottomissione sottomissione = teamIscritto.getSottomissione();
        if (sottomissione == null) {
            sottomissione = new Sottomissione();
            teamIscritto.setSottomissione(sottomissione);
        }

        sottomissione.setFile(sottomissioneFile);
        sottomissione.setDataCaricamento(LocalDateTime.now());
        sottomissione.setStatoSottomissione(StatoSottomissione.CARICATA);
    }


    public void valutaSottomissione(TeamIscritto teamIscritto, Valutazione valutazione) {
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto nullo");
        }

        Sottomissione sottomissione = teamIscritto.getSottomissione();
        if (sottomissione == null || sottomissione.getStatoSottomissione() == StatoSottomissione.MANCANTE) {
            // coerente al senso del caso d’uso: non puoi valutare se non c'è una submission caricata
            throw new IllegalStateException("Impossibile valutare: sottomissione mancante");
        }

        if (valutazione == null) {
            throw new IllegalArgumentException("Valutazione nulla");
        }

        sottomissione.setValutazione(valutazione);
        sottomissione.setStatoSottomissione(StatoSottomissione.VALUTATA);
    }
}
