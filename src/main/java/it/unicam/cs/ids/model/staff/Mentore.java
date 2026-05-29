package it.unicam.cs.ids.model.staff;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;

import java.util.ArrayList;
import java.util.List;

public class Mentore extends RuoloPartecipazione {
    List<TeamIscritto> teamAssociati = new ArrayList<>();

    private String mentoreID;
    private List<TeamIscritto> listaTeamAssegnati = new ArrayList<>();

    public Mentore(Utente utente, Hackathon hackathon) {
        super(utente, hackathon);

    }

    @Override
    public RuoliStaff getTipoRuolo() {
        return RuoliStaff.MENTORE;
    }

    public String getMentoreID() {
        return mentoreID;
    }

    public void setMentoreID(String mentoreID) {
        this.mentoreID = mentoreID;
    }

    public List<TeamIscritto> getTeamAssegnati() {
        return listaTeamAssegnati;
    }

    public void aggiungiTeamAssegnato(TeamIscritto team) {
        listaTeamAssegnati.add(team);
    }
}
