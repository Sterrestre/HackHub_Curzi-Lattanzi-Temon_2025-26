package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

public class InvitoStaff extends Invito {
    private final Utente mittente;
    private final Hackathon hackathon;
    private final RuoliStaff ruolo;
    private final LocalDateTime scadenza;


    public InvitoStaff(Utente mittente, Utente destinatario, Hackathon hackathon, RuoliStaff ruolo, LocalDateTime scadenza) {
        super(destinatario);
        this.mittente = mittente;
        this.hackathon = hackathon;
        this.ruolo = ruolo;
        this.scadenza = scadenza;
    }


    public Utente getMittente() {
        return mittente;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public RuoliStaff getRuolo() {
        return ruolo;
    }

    public LocalDateTime getScadenza() {
        return scadenza;
    }
}
