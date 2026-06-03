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

    // TODO: controlla questo permesso. Da noi un utente che era precedentemente stato membro di staff in un hackathon
    //  può creare un hackathon, e al momento della creazione diventa ORGANIZZATORE. Ha senso questo permesso?
    @Override
    public boolean puoCreareHackathon() { return true; }

    @Override
    public boolean puoGestireSottomissioni() { return true; }
}
