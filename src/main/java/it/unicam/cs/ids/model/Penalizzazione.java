package it.unicam.cs.ids.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Penalizzazione {

    private String teamID;
    private String tipoIntervento;
    private String motivazione;

    // COSTRUTTORE PER JPA
    protected Penalizzazione() {}

    public Penalizzazione(String teamID, String tipoIntervento, String motivazione) {
        this.teamID = teamID;
        this.tipoIntervento = tipoIntervento;
        this.motivazione = motivazione;
    }

    public String getTeamID() {
        return teamID;
    }

    public String getTipoIntervento() {
        return tipoIntervento;
    }

    public String getMotivazione() {
        return motivazione;
    }
}
