package it.unicam.cs.ids.model;

public class Penalizzazione {

    private long teamID;
    private String tipoIntervento;
    private String motivazione;

    public Penalizzazione(long teamID, String tipoIntervento, String motivazione) {
        this.teamID = teamID;
        this.tipoIntervento = tipoIntervento;
        this.motivazione = motivazione;
    }

    public long getTeamID() {
        return teamID;
    }

    public String getTipoIntervento() {
        return tipoIntervento;
    }

    public String getMotivazione() {
        return motivazione;
    }
}
