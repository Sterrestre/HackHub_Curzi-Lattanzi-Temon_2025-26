package it.unicam.cs.ids.model;

import it.unicam.cs.ids.model.team.TeamIscritto;

import java.util.UUID;

public class RichiestaSupporto {

    private String richiestaSuppID;
    private String dettagli;
    private boolean visualizzata = false; // false = non visualizzata
    private TeamIscritto teamIscritto;

    public RichiestaSupporto(String dettagli, TeamIscritto teamIscritto) {
        this.richiestaSuppID = UUID.randomUUID().toString();
        this.dettagli = dettagli;
        this.teamIscritto = teamIscritto;
    }

    public String  getRichiestaSuppID() {
        return richiestaSuppID;
    }

    public boolean isVisualizzata() {
        return visualizzata;
    }

    public void modificaStato(boolean visualizzata) {
        this.visualizzata = visualizzata;
    }

    public String getDettagli() {
        return dettagli;

    }

    public TeamIscritto getTeamIscritto() {
        return teamIscritto;
    }

    public String getHackathonId() {
        if (teamIscritto == null || teamIscritto.getHackathon() == null) {
            throw new IllegalStateException("Richiesta di supporto non collegata a un team iscrittto");
        }
        return teamIscritto.getHackathon().getHackathonID();
    }
}