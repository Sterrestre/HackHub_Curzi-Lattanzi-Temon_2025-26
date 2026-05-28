package it.unicam.cs.ids.model.team;

import it.unicam.cs.ids.model.Utente;

public class MembroTeam {
    private Utente utente;
    private Team team;
    private boolean amministratore;

    MembroTeam(Utente utente, Team team, boolean amministratore) {
        if (utente == null) throw new NullPointerException("L'utente non puo' essere null!");
        if (team == null) throw new NullPointerException("Il team non puo' essere null!");

        this.utente = utente;
        this.team = team;
        this.amministratore = amministratore;
    }

    public Utente getUtente() {
        return  this.utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isAmministratore() {
        return this.amministratore;
    }

    public void setAmministratore(boolean amministratore) {
        this.amministratore = amministratore;
    }
}
