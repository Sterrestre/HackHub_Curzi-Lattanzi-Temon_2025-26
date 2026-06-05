package it.unicam.cs.ids.model.staff;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MENTORE")
public class Mentore extends RuoloPartecipazione {

    private String mentoreID;
    @ManyToMany
    @JoinTable(name = "mentore_team",
            joinColumns = @JoinColumn(name = "mentore_id"),
            inverseJoinColumns = @JoinColumn(name = "team_id"))
    private List<TeamIscritto> listaTeamAssegnati = new ArrayList<>();

    // COSTRUTTORE JPA
    protected Mentore() {}

    public Mentore(Utente utente, Hackathon hackathon) {
        super(utente, hackathon);

    }

    @Override
    public RuoliStaff getTipoRuolo() {
        return RuoliStaff.MENTORE;
    }

    @Override
    public boolean puoMentorare() { return true; }

    @Override
    public boolean puoPenalizzare() { return true; }

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
