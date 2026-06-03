package it.unicam.cs.ids.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Conto {

    private String iban;
    private String intestatario;

    // COSTRUTTORE PER JPA
    protected Conto() {}

    public Conto(String iban, String intestatario) {
        this.iban = iban;
        this.intestatario = intestatario;
    }

    public String getIban() {
        return iban;
    }

    public String getIntestatario() {
        return intestatario;
    }

    public void cambiaIban(String nuovoIban) {
        if (nuovoIban == null || nuovoIban.isBlank()) {
            throw new IllegalArgumentException("IBAN non valido");
        }
        this.iban = nuovoIban;
    }

}
