package it.unicam.cs.ids.model;

import java.util.Objects;

public class Valutazione {
    private int voto;
    private String giudizio;

    public Valutazione(int voto, String giudizio) {
        this.voto = voto;
        this.giudizio = giudizio;
    }

    public int getVoto() {
        return voto;
    }

    public String getGiudizio() {
        return giudizio;
    }

    public void setVoto(int voto) {
        if (voto < 0 || voto > 10) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 0 e 10.");
        }
        this.voto = voto;
    }
    public void setGiudizio(String giudizio) {
        if (giudizio == null || giudizio.isEmpty()) {   // oppure giudizion.trim().isEmpty() per escludere stringhe composte solo da spazi
            throw new IllegalArgumentException("Il giudizio non può essere null o vuoto.");
        }
        this.giudizio = giudizio;
    }

        @Override
    public boolean equals( Object e) {
        if (!(e instanceof Valutazione other)) {
            return false;
        }
        return this.voto == other.voto && Objects.equals(giudizio,other.giudizio);
    }


    @Override
    public int hashCode() {
        return Objects.hash(voto, giudizio);
    }

    @Override
    public String toString() {
        return "Valutazione{voto=" + voto + ", giudizio='" + giudizio + "'}";
    }
}
