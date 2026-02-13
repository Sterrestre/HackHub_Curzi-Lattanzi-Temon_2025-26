package it.unicam.cs.ids.model;

public abstract class RuoloPartecipazione {

    protected Utente utente;
    protected Hackathon hackathon;

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
}
