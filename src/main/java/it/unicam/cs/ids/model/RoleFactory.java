package it.unicam.cs.ids.model;

public class RoleFactory {

    public Organizzatore assegnaOrganizzatore(Utente utente, Hackathon hackathon) {
        Organizzatore o = new Organizzatore(utente, hackathon);
        utente.addRuolo(o);
        return o;
    }

    public Giudice assegnaGiudice(Utente utente, Hackathon hackathon) {
        Giudice g = new Giudice(utente, hackathon);
        utente.addRuolo(g);
        return g;
    }

    public Mentore assegnaMentore(Utente utente, Hackathon hackathon) {
        Mentore m = new Mentore(utente, hackathon);
        utente.addRuolo(m);
        return m;
    }
}


