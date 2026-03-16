package it.unicam.cs.ids.model;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class InfoHack {

    private String nome;
    private String regolamento;
    private LocalDate dataInizio;

    public String getRegolamento() {
        return regolamento;
    }

    public LocalDate getDataInizio() {
        return dataInizio;
    }

    public LocalDate getDataFine() {
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

    private LocalDate dataFine;
    private String luogo;
    private LocalDateTime scadenzaIscrizioni;
    private double quotaIscrizione;
    private double premio;
    private int dimMaxTeam;
    private int numMaxTeam;


    public void setRegolamento(String regolamento) {
        this.regolamento = regolamento;
    }

    public void setDataInizio(LocalDate dataInizio) {
        this.dataInizio = dataInizio;
    }

    public void setDataFine(LocalDate dataFine) {
        this.dataFine = dataFine;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public void setScadenzaIscrizioni(LocalDateTime scadenzaIscrizioni) {
        this.scadenzaIscrizioni = scadenzaIscrizioni;
    }

    public void setQuotaIscrizione(double quotaIscrizione) {
        this.quotaIscrizione = quotaIscrizione;
    }

    public void setPremio(double premio) {
        this.premio = premio;
    }

    public void setDimMaxTeam(int dimMaxTeam) {
        this.dimMaxTeam = dimMaxTeam;
    }

    public void setNumMaxTeam(int numMaxTeam) {
        this.numMaxTeam = numMaxTeam;
    }

}
