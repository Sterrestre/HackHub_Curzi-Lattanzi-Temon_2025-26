package it.unicam.cs.ids.service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <<Service>>
 * Rappresenta il sistema esterno per la gestione delle call (Calendar).
 */
public class sistemaCall {

    /**
     * Genera un collegamento per una call
     */
    public String generaCollegamento(LocalDateTime dataOra) {

        // simulazione creazione link (tipo Google Meet / Zoom)
        String link = "https://calendar.call/" + UUID.randomUUID();

        System.out.println("Collegamento generato per la call: " + link);

        return link;
    }
}