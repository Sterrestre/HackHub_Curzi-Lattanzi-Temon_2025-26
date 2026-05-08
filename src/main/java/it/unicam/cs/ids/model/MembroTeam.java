package it.unicam.cs.ids.model;

public class MembroTeam {

    private Utente utente;
    private Team team;
    private boolean amministratore;

    // -------------------------
    //       COSTRUTTORE
    // -------------------------
    public MembroTeam(Utente utente, Team team, boolean amministratore) {
        this.utente = utente;
        this.team = team;
        this.amministratore = amministratore;
    }

    public Utente getUtente() {
        return utente;
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
        return amministratore;
    }

    public void setAmministratore(boolean amministratore) {
        this.amministratore = amministratore;
    }
}
