package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

public class InvitoStaff extends Invito {
    private final Utente daParteDi;
    private final Hackathon hackathon;
    private final RuoliStaff ruolo;
    private final LocalDateTime scadenza;


    public InvitoStaff(Utente daParteDi, Utente destinatario, Hackathon hackathon, RuoliStaff ruolo, LocalDateTime scadenza) {
        super(destinatario);
        this.daParteDi = daParteDi;
        this.hackathon = hackathon;
        this.ruolo = ruolo;
        this.scadenza = scadenza;
    }


    public Utente getMittente() {
        return daParteDi;
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
