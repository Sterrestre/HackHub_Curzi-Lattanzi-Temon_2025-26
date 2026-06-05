package it.unicam.cs.ids.model.inviti;

import it.unicam.cs.ids.model.Utente;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Rappresenta un invito destinato a un utente del sistema.
 *
 * Classe astratta che contiene i dati comuni a tutti i tipi di invito
 * (ad esempio il destinatario e la data di creazione) e dichiara i
 * comportamenti astratti che le sottoclassi devono implementare:
 */

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_invito", discriminatorType = DiscriminatorType.STRING)
public abstract class Invito {
    /** ID univoco generato automaticamente da JPA. */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /** Utente destinatario dell'invito. */
    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    protected Utente destinatario;
    protected LocalDateTime dataCreazione;

    // COSTRUTTORE PER JPA
    protected Invito() {}

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
     * Restituisce l'ID univoco dell'invito.
     *
     * @return l'ID dell'invito
     */
    public String getId() {
        return id;
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
