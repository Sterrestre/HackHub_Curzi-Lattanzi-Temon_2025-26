package it.unicam.cs.ids.model.inviti;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.staff.RuoliStaff;

import java.time.LocalDateTime;

public class InvitoHackathon extends Invito {
    private final Utente mittente;
    private final Hackathon hackathon;
    private final RuoliStaff ruolo;
    private final LocalDateTime scadenza;


    public InvitoHackathon(Utente mittente, Utente destinatario, Hackathon hackathon, RuoliStaff ruolo, LocalDateTime scadenza) {
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
