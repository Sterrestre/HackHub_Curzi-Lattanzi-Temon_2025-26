package it.unicam.cs.ids.model;

import java.util.UUID;

public class RichiestaSupporto {

    // TODO collegare la richiesta di supporto al TeamIscritto (poi l'hackathon si ricava da lì eventualmente)
    private String richiestaSuppID;
    private String dettagli;
    private boolean visualizzata = false; // false = non visualizzata

    public RichiestaSupporto(String dettagli) {
        this.richiestaSuppID = UUID.randomUUID().toString();
        this.dettagli = dettagli;
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
}