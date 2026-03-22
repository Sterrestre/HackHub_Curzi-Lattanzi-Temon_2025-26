package it.unicam.cs.ids.model;

public class RoleFactory {

    public RuoloPartecipazione creaERegistraRuolo(RuoliStaff ruoloStaff, Utente utente, Hackathon hackathon) {
        RuoloPartecipazione ruolo = creaRuolo(ruoloStaff, utente, hackathon);
        hackathon.ruoli.add(ruolo);
        return ruolo;
    }

    private RuoloPartecipazione creaRuolo(RuoliStaff ruolo, Utente utente, Hackathon hackathon) {
        return switch (ruolo) {
            case ORGANIZZATORE -> assegnaOrganizzatore(utente, hackathon);
            case GIUDICE -> assegnaGiudice(utente, hackathon);
            case MENTORE -> assegnaMentore(utente, hackathon);
            default -> throw new IllegalArgumentException("Tipo di ruolo non supportato: " + ruolo);
        };
    }

    private Organizzatore assegnaOrganizzatore(Utente utente, Hackathon hackathon) {
        Organizzatore o = new Organizzatore(utente, hackathon);
        utente.addRuolo(o);
        return o;
    }

    private Giudice assegnaGiudice(Utente utente, Hackathon hackathon) {
        Giudice g = new Giudice(utente, hackathon);
        utente.addRuolo(g);
        return g;
    }

    private Mentore assegnaMentore(Utente utente, Hackathon hackathon) {
        Mentore m = new Mentore(utente, hackathon);
        utente.addRuolo(m);
        return m;
    }
}


