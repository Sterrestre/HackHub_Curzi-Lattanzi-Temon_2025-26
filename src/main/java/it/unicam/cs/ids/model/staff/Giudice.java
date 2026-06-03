package it.unicam.cs.ids.model.staff;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("GIUDICE")
public class Giudice extends RuoloPartecipazione{

    // COSTRUTTORE JPA
    protected Giudice() {}

    public Giudice(Utente utente, Hackathon hackathon) {
        super(utente, hackathon);
    }

    @Override
    public RuoliStaff getTipoRuolo() {
        return RuoliStaff.GIUDICE;
    }

    @Override
    public boolean puoValutare() { return true; }

    @Override
    public boolean puoGestireSottomissioni() { return true; }

}
