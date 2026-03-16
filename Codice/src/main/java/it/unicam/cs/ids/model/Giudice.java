package it.unicam.cs.ids.model;

public class Giudice extends RuoloPartecipazione{

    public Giudice(Utente utente, Hackathon hackathon) {
        super(utente, hackathon);
    }

    @Override
    public RuoliStaff getTipoRuolo() {
        return RuoliStaff.GIUDICE;
    }
}
