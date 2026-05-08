package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

/**
 * Rappresenta un invito destinato a un utente del sistema.
 *
 * Classe astratta che contiene i dati comuni a tutti i tipi di invito
 * (ad esempio il destinatario e la data di creazione) e dichiara i
 * comportamenti astratti che le sottoclassi devono implementare:
 */
public abstract class Invito {
    protected Utente destinatario;
    protected Utente daParteDi;
    protected LocalDateTime dataCreazione;

    /**
     * Costruttore per creare un invito.
     *
     * @param destinatario l'utente destinatario dell'invito; non deve essere {@code null}
     */
    public Invito(Utente destinatario) {
        if (destinatario == null) {
            throw new IllegalArgumentException("Il destinatario dell'invito non può essere null.");
        }
        this.destinatario = destinatario;
        this.dataCreazione = LocalDateTime.now();
    }


    /**
     * Restituisce il destinatario dell'invito.
     *
     * @return l'utente destinatario
     */
    public Utente getDestinatario() {
        return destinatario;
    }


    /**
     * Restituisce la data e ora di creazione dell'invito.
     *
     * @return la data e ora di creazione
     */
    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }
}
