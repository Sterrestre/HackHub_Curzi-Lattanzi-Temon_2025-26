package it.unicam.cs.ids.model.staff;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class RuoloPartecipazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @ManyToOne(optional = false)
    protected Utente utente;

    @ManyToOne(optional = false)
    protected Hackathon hackathon;

    //COSTRUTTORE JPA
    protected RuoloPartecipazione() {}

    protected RuoloPartecipazione(Utente utente, Hackathon hackathon){
        this.utente = utente;
        this.hackathon = hackathon;
    }

    public Utente getUtente() {
        return utente;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public abstract RuoliStaff getTipoRuolo();

    // Permessi
    public boolean puoCreareHackathon() { return false; }
    public boolean puoValutare() { return false; }
    public boolean puoMentorare() { return false; }
    public boolean puoPenalizzare() { return false; }
    public boolean puoGestireSottomissioni() { return false; }
}
