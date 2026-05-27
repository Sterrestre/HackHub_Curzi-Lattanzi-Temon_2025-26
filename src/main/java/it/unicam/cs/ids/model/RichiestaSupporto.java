package it.unicam.cs.ids.model;

public class RichiestaSupporto {

    private String dettagli;
    private boolean visualizzata = false; // false = non visualizzata

    public RichiestaSupporto(String dettagli) {
        this.dettagli = dettagli;
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