package it.unicam.cs.ids.model.staff;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;

public class Giudice extends RuoloPartecipazione{

    public Giudice(Utente utente, Hackathon hackathon) {
        super(utente, hackathon);
    }

    @Override
    public RuoliStaff getTipoRuolo() {
        return RuoliStaff.GIUDICE;
    }
}
