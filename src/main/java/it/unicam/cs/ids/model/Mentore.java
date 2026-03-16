package it.unicam.cs.ids.model;

public class Mentore extends RuoloPartecipazione {

    public Mentore(Utente utente, Hackathon hackathon) {
        super(utente, hackathon);
    }

    @Override
    public RuoliStaff getTipoRuolo() {
        return RuoliStaff.MENTORE;
    }
}
