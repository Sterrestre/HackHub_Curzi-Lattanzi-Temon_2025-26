package it.unicam.cs.ids.model.hackathon;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

@Embeddable
public class InfoHack {
    @Column(name = "info_nome")
    private String nome;
    private String regolamento;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;
    private String luogo;
    private LocalDateTime scadenzaIscrizioni;
    private double quotaIscrizione;
    private double premio;
    private int dimMaxTeam;
    private int numMaxTeam;

    // COSTRUTTORE JPA
    protected InfoHack() {}

    public String getRegolamento() {
        return regolamento;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public String getLuogo() {
        return luogo;
    }

    public LocalDateTime getScadenzaIscrizioni() {
        return scadenzaIscrizioni;
    }

    public double getQuotaIscrizione() {
        return quotaIscrizione;
    }

    public double getPremio() {
        return premio;
    }

    public int getDimMaxTeam() {
        return dimMaxTeam;
    }

    public int getNumMaxTeam() {
        return numMaxTeam;
    }


    /**
     * Imposta il nome dell'hackathon.
     * @throws IllegalArgumentException se il nome è null o vuoto
     */
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome non può essere null o vuoto");
        }
        this.nome = nome;
    }

    /**
     * Imposta il regolamento dell'hackathon.
     * @throws IllegalArgumentException se il regolamento è null o vuoto
     */
    public void setRegolamento(String regolamento) {
        if (regolamento == null || regolamento.isBlank()) {
            throw new IllegalArgumentException("Il regolamento non può essere null o vuoto");
        }
        this.regolamento = regolamento;
    }

    /**
     * Imposta la data di inizio. Deve essere nel futuro e antecedente alla data di fine
     * (se già impostata). La scadenza iscrizioni (se già impostata) deve restare antecedente.
     * @throws IllegalArgumentException se la data è null, già passata o viola la coerenza temporale
     */
    public void setDataInizio(LocalDateTime dataInizio) {
        int giorniScadenzaHackathon = Integer.parseInt(
                System.getProperty("crea.hack.giorni", "14")
        );

        if (dataInizio == null) {
            throw new IllegalArgumentException("La data di inizio non può essere null");
        }
        if (dataInizio.isBefore(LocalDateTime.now().plusDays(giorniScadenzaHackathon))) {
            throw new IllegalArgumentException("La data di inizio non può essere nel passato");
        }
        if (dataFine != null && !dataInizio.isBefore(dataFine)) {
            throw new IllegalArgumentException("La data di inizio deve essere antecedente alla data di fine");
        }
        if (scadenzaIscrizioni != null && !scadenzaIscrizioni.toLocalDate().isBefore(ChronoLocalDate.from(dataInizio))) {
            throw new IllegalArgumentException("La scadenza delle iscrizioni deve essere antecedente alla data di inizio");
        }
        this.dataInizio = dataInizio;
    }

    /**
     * Imposta la data di fine. Deve essere successiva alla data di inizio (se già impostata).
     * @throws IllegalArgumentException se la data è null o non rispetta la coerenza temporale
     */
    public void setDataFine(LocalDateTime dataFine) {
        if (dataFine == null) {
            throw new IllegalArgumentException("La data di fine non può essere null");
        }
        if (dataInizio != null && dataFine.isBefore(dataInizio)) {
            throw new IllegalArgumentException("La data di fine deve essere successiva o uguale alla data di inizio");
        }
        this.dataFine = dataFine;
    }

    /**
     * Imposta il luogo dell'hackathon.
     * @throws IllegalArgumentException se il luogo è null o vuoto
     */
    public void setLuogo(String luogo) {
        if (luogo == null || luogo.isBlank()) {
            throw new IllegalArgumentException("Il luogo non può essere null o vuoto");
        }
        this.luogo = luogo;
    }

    /**
     * Imposta la scadenza delle iscrizioni. Deve essere futura e antecedente alla data di inizio
     * (se già impostata).
     * @throws IllegalArgumentException se la scadenza è null, già passata o viola la coerenza temporale
     */
    public void setScadenzaIscrizioni(LocalDateTime scadenzaIscrizioni) {
        if (scadenzaIscrizioni == null) {
            throw new IllegalArgumentException("La scadenza delle iscrizioni non può essere null");
        }
        if (scadenzaIscrizioni.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La scadenza delle iscrizioni non può essere nel passato");
        }
        if (dataInizio != null && !scadenzaIscrizioni.toLocalDate().isBefore(ChronoLocalDate.from(dataInizio))) {
            throw new IllegalArgumentException("La scadenza delle iscrizioni deve essere antecedente alla data di inizio");
        }
        this.scadenzaIscrizioni = scadenzaIscrizioni;
    }

    /**
     * Imposta la quota di iscrizione. Deve essere >= 0 (0 = gratuito).
     * @throws IllegalArgumentException se la quota è negativa
     */
    public void setQuotaIscrizione(double quotaIscrizione) {
        if (quotaIscrizione < 0) {
            throw new IllegalArgumentException("La quota di iscrizione non può essere negativa");
        }
        if(quotaIscrizione > 0) { // implementare logica
        }
        this.quotaIscrizione = quotaIscrizione;
    }

    /**
     * Imposta il premio. Deve essere >= 0 (0 = nessun premio monetario).
     * @throws IllegalArgumentException se il premio è negativo
     */
    public void setPremio(double premio) {
        if (premio < 0) {
            throw new IllegalArgumentException("Il premio non può essere negativo");
        }
        this.premio = premio;
    }

    /**
     * Imposta la dimensione massima del team. Deve essere >= 1.
     * @throws IllegalArgumentException se la dimensione è minore di 1
     */
    public void setDimMaxTeam(int dimMaxTeam) {
        if (dimMaxTeam < 1) {
            throw new IllegalArgumentException("La dimensione massima del team deve essere almeno 1");
        }
        this.dimMaxTeam = dimMaxTeam;
    }

    /**
     * Imposta il numero massimo di team. Deve essere >= 2.
     * @throws IllegalArgumentException se il numero è minore di 2
     */
    public void setNumMaxTeam(int numMaxTeam) {
        if (numMaxTeam < 2) {
            throw new IllegalArgumentException("Il numero massimo di team deve essere almeno 2");
        }
        this.numMaxTeam = numMaxTeam;
    }

    public static class Builder implements InfoHackBuilder {

        private final InfoHack info = new InfoHack();

        @Override
        public InfoHackBuilder regolamento(String regolamento) {
            info.setRegolamento(regolamento);
            return this;
        }

        @Override
        public InfoHackBuilder dataInizio(LocalDateTime data) {
            info.setDataInizio(data);
            return this;
        }

        @Override
        public InfoHackBuilder dataFine(LocalDateTime data) {
            info.setDataFine(data);
            return this;
        }

        @Override
        public InfoHackBuilder luogo(String luogo) {
            info.setLuogo(luogo);
            return this;
        }

        @Override
        public InfoHackBuilder scadenzaIscrizioni(LocalDateTime scadenza) {
            info.setScadenzaIscrizioni(scadenza);
            return this;
        }

        @Override
        public InfoHackBuilder quotaIscrizione(double quota) {
            info.setQuotaIscrizione(quota);
            return this;
        }

        @Override
        public InfoHackBuilder premio(double premio) {
            info.setPremio(premio);
            return this;
        }

        @Override
        public InfoHackBuilder dimMaxTeam(int dim) {
            info.setDimMaxTeam(dim);
            return this;
        }

        @Override
        public InfoHackBuilder numMaxTeam(int num) {
            info.setNumMaxTeam(num);
            return this;
        }

        @Override
        public InfoHack build() {
            return info;
        }
    }
}
