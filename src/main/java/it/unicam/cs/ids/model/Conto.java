package it.unicam.cs.ids.model;

public class Conto {

    private String iban;
    private String intestatario;

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
