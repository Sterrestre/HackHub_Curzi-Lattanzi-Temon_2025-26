package it.unicam.cs.ids.model.staff;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ORGANIZZATORE")
public class Organizzatore extends RuoloPartecipazione {

    //COSTRUTTORE JPA
    protected Organizzatore() {}

    public Organizzatore(Utente utente, Hackathon hackathon) {
        super(utente, hackathon);
    }

    @Override
    public RuoliStaff getTipoRuolo() {
        return RuoliStaff.ORGANIZZATORE;
    }

    @Override
    public boolean puoGestireSottomissioni() { return true; }
}
