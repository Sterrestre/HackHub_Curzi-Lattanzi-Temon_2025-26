package it.unicam.cs.ids.model;

import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Rappresenta una sottomissione di un team a un hackathon.
 * Gestisce titolo, descrizione, link repository, lo stato della valutazione,
 * la data di caricamento e la relativa valutazione.
 */
@Entity
public class Sottomissione {

    /** Identificatore univoco della sottomissione */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String sottomissioneID;

    /** Titolo della sottomissione */
    private String titolo;

    /** Descrizione della sottomissione */
    private String descrizione;

    /** Link al repository del progetto */
    private String linkRepository;

    /** Stato corrente della sottomissione */
    @Enumerated(EnumType.STRING)
    private StatoSottomissione statoSottomissione = StatoSottomissione.MANCANTE;

    /** Valutazione associata alla sottomissione */
    @Embedded
    private Valutazione valutazione;

    /** Data e ora del caricamento del file */
    private LocalDateTime dataCaricamento;

    /** Team associato alla sottomissione */
    @OneToOne
    @JoinColumn(name = "team_id")
    private TeamIscritto team;

    /** Hackathon per cui è stata effettuata la sottomissione */
    @ManyToOne
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;

    // COSTRUTTORE PER JPA
    protected Sottomissione() {}

    /**
     * Costruisce una nuova Sottomissione associata al team iscritto corrispondente.
     *
     * @param teamIscritto il team iscritto a cui è associata la sottomissione
     */
    public Sottomissione(TeamIscritto teamIscritto) {
        this.team = teamIscritto;
    }

    public TeamIscritto getTeam() {
        return team;
    }

    public void setTeam(TeamIscritto team) {
        this.team = team;
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
    public LocalDateTime getDataCaricamento() {
        return dataCaricamento;
    }

    /**
     * Imposta la data e l'ora del caricamento della sottomissione.
     *
     * @param dataCaricamento la data di caricamento
     * @throws IllegalArgumentException se dataCaricamento è null
     */
    public void setDataCaricamento(LocalDateTime dataCaricamento) {
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
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Sottomissione other)) {
            return false;
        }
        return Objects.equals(sottomissioneID, other.sottomissioneID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sottomissioneID);
    }

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
     */
    public String getId() {
        return sottomissioneID;
    }

    public String getTitolo() { return titolo; }

    public void setTitolo(String titolo) { this.titolo = titolo; }

    public String getDescrizione() { return descrizione; }

    public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

    public String getLinkRepository() { return linkRepository; }

    public void setLinkRepository(String linkRepository) { this.linkRepository = linkRepository; }

    public String titolo() { return titolo; }

    public String descrizione() { return descrizione; }

    public String linkRepository() { return linkRepository; }
}