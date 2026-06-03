package it.unicam.cs.ids.model;

import it.unicam.cs.ids.model.team.TeamIscritto;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Rappresenta una sottomissione di un team a un hackathon.
 * Gestisce il file submitted, lo stato della valutazione, la data di caricamento e la relativa valutazione.
 */
public class Sottomissione {
    /** Identificatore univoco della sottomissione */
    private final String sottomissioneID;

    /** File della sottomissione */
    private File file;

    /** Stato corrente della sottomissione */
    private StatoSottomissione statoSottomissione = StatoSottomissione.MANCANTE;

    /** Valutazione associata alla sottomissione */
    private Valutazione valutazione;

    /** Data e ora del caricamento del file */
    private LocalDateTime dataCaricamento;

    /** Team associato alla sottomissione */
    private TeamIscritto team;

    /**
     * Costruisce una nuova Sottomissione con l'ID specificato.
     *
     * @param sottomissioneID identificatore univoco della sottomissione
     * @throws IllegalArgumentException se sottomissioneID è null
     */
    public Sottomissione(String sottomissioneID) {
        if (sottomissioneID == null) {
            throw new IllegalArgumentException("L'ID della sottomissione non può essere null.");
        }
        this.sottomissioneID = sottomissioneID;
    }

    /**
     * Restituisce l'identificatore univoco della sottomissione.
     *
     * @return l'ID della sottomissione
     */
    public String getSottomissioneID() {
        return sottomissioneID;
    }

    /**
     * Restituisce il file della sottomissione.
     *
     * @return il file della sottomissione, o null se non impostato
     */
    public File getFile() {
        return file;
    }

    /**
     * Imposta il file della sottomissione.
     *
     * @param file il file della sottomissione
     * @throws IllegalArgumentException se file è null
     */
    public void setFile(File file) {
        if (file == null) {
            throw new IllegalArgumentException("Il file della sottomissione non può essere null.");
        }
        this.file = file;
    }

    /**
     * Restituisce lo stato corrente della sottomissione.
     *
     * @return lo stato della sottomissione
     */
    public StatoSottomissione getStatoSottomissione() {
        return statoSottomissione;
    }

    /**
     * Imposta lo stato della sottomissione.
     *
     * @param statoSottomissione il nuovo stato della sottomissione
     * @throws IllegalArgumentException se statoSottomissione è null
     */
    public void setStatoSottomissione(StatoSottomissione statoSottomissione) {
        if (statoSottomissione == null) {
            throw new IllegalArgumentException("Lo stato della sottomissione non può essere null.");
        }
        this.statoSottomissione = statoSottomissione;
    }
    /**
     * Restituisce la data e l'ora del caricamento della sottomissione.
     *
     * @return la data di caricamento, o null se non impostata
     */
    public LocalDateTime getDataCaricamento(){
        return dataCaricamento;
    }

    /**
     * Imposta la data e l'ora del caricamento della sottomissione.
     *
     * @param dataCaricamento la data di caricamento
     * @throws IllegalArgumentException se dataCaricamento è null
     */
    public void setDataCaricamento(LocalDateTime dataCaricamento){
        if (dataCaricamento == null) {
            throw new IllegalArgumentException("La data di caricamento della sottomissione non può essere null.");
        }
        this.dataCaricamento = dataCaricamento;
    }
    /**
     * Restituisce la valutazione associata alla sottomissione.
     *
     * @return la valutazione, o null se non effettuata
     */
    public Valutazione getValutazione() {
        return valutazione;
    }
    public void setValutazione(Valutazione valutazione) {
        if (valutazione == null) {
            throw new IllegalArgumentException("La valutazione della sottomissione non può essere null.");
        }
        this.valutazione = valutazione;
    }

    /**
     * Verifica se la sottomissione risulta valutata.
     *
     * @return {@code true} se lo stato e' {@link StatoSottomissione#VALUTATA}, altrimenti {@code false}
     */
    public boolean isValutata() {
        return statoSottomissione == StatoSottomissione.VALUTATA;
    }

    /**
     * Confronta due sottomissioni in base al loro identificatore univoco.
     *
     * @param o oggetto da confrontare con questa sottomissione
     * @return {@code true} se gli ID coincidono, altrimenti {@code false}
     */
    @Override
    public boolean equals(Object o) {
        if(!(o instanceof Sottomissione other)) {
            return false;
        }
        return Objects.equals(sottomissioneID, other.sottomissioneID);
    }

    /**
     * Restituisce l'hash code coerente con l'implementazione di {@link #equals(Object)}.
     *
     * @return hash code calcolato a partire da {@code sottomissioneID}
     */
    @Override
    public int hashCode() {
        return Objects.hash(sottomissioneID);
    }


    /**
     * Restituisce una rappresentazione testuale sintetica della sottomissione.
     *
     * @return stringa con ID, stato e data di caricamento
     */
    @Override
    public String toString() {
        return "Sottomissione{id='" + sottomissioneID + "', stato=" + statoSottomissione + ", data=" + dataCaricamento + "}";
    }


    /**
     * Restituisce il giudizio della valutazione associata.
     *
     * @return giudizio testuale della valutazione
     * @throws IllegalStateException se la sottomissione non e' ancora stata valutata
     */
    public String getGiudizio() {
        if (valutazione == null) {
            throw new IllegalStateException("Impossibile ottenere il giudizio: sottomissione non valutata");
        }
        return valutazione.getGiudizio();
    }

    /**
     * Restituisce l'identificatore univoco della sottomissione.
     *
     * @return ID della sottomissione
     */
    public String getId() {
        return sottomissioneID;
    }

    public String titolo() { return file != null ? file.getName() : null; }

    public String descrizione() { return file != null ? file.getAbsolutePath() : null; }

    public Object linkRepository() { return null; }
}