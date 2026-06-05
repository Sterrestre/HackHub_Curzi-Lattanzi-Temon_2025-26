package it.unicam.cs.ids.model.inviti;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.staff.RuoliStaff;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Invito inviato dall'organizzatore a un utente per ricoprire
 * un ruolo di staff (mentore o giudice) in un hackathon.
 */
@Entity
@DiscriminatorValue("HACKATHON")
public class InvitoHackathon extends Invito {
    /** Organizzatore che ha inviato l'invito. */
    @ManyToOne
    @JoinColumn(name = "mittente_id")
    private Utente mittente;

    /** Hackathon per cui è stato inviato l'invito. */
    @ManyToOne
    @JoinColumn(name = "hackathon_id")
    private Hackathon hackathon;

    /** Ruolo per cui l'utente è stato invitato (MENTORE o GIUDICE). */
    @Enumerated(EnumType.STRING)
    private RuoliStaff ruolo;

    /** Data oltre la quale l'invito non è più valido. */
    private LocalDateTime scadenza;

    // COSTRUTTORE PER JPA
    protected InvitoHackathon() {}

    public InvitoHackathon(Utente mittente, Utente destinatario, Hackathon hackathon, RuoliStaff ruolo, LocalDateTime scadenza) {
        super(destinatario);
        this.mittente = mittente;
        this.hackathon = hackathon;
        this.ruolo = ruolo;
        this.scadenza = scadenza;
    }


    public Utente getMittente() {
        return mittente;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public RuoliStaff getRuolo() {
        return ruolo;
    }

    public LocalDateTime getScadenza() {
        return scadenza;
    }
}
