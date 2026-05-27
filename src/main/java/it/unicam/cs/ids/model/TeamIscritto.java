package it.unicam.cs.ids.model;

import java.util.ArrayList;
import java.util.List;

public class TeamIscritto {

    private Team team;
    private Hackathon hackathon;
    private Utente amministratore;
    private List<MembroTeamIscritto> elencoIscritti = new ArrayList<>();
    private Sottomissione sottomissione;

    public TeamIscritto(Team team, Hackathon hackathon, Utente amministratore) {
        this.team = team;
        this.hackathon = hackathon;
        this.amministratore = amministratore;
    }

    public Team getTeam() {
        return team;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public Utente getAmministratore() {
        return amministratore;
    }

    public List<MembroTeamIscritto> getElencoIscritti() {
        return elencoIscritti;
    }

    public Sottomissione getSottomissione() {
        return sottomissione;
    }

    public void aggiungiIscritto(MembroTeamIscritto membro) {
        if (!elencoIscritti.contains(membro)) {
            elencoIscritti.add(membro);
        }
    }

    public void rimuoviIscritto(MembroTeamIscritto membro) {
        elencoIscritti.remove(membro);
    }

    public void inviaSottomissione(Sottomissione sottomissione) {
        this.sottomissione = sottomissione;
    }

    public int getNumIscritti() {
        return elencoIscritti.size();
    }

    public void setSottomissione(Sottomissione sottomissione) {
    }

    public boolean getId() {
        return false;
    }
}
