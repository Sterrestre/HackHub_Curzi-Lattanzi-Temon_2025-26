package it.unicam.cs.ids.model;

public class Organizzatore extends RuoloPartecipazione {

    public Organizzatore(Utente utente, Hackathon hackathon) {
        super(utente, hackathon);
    }

    @Override
    public RuoliStaff getTipoRuolo() {
        return RuoliStaff.ORGANIZZATORE;
    }
}
