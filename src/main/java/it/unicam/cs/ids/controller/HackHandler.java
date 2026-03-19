package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.*;
import java.time.LocalDateTime;
import java.util.List;


public class HackHandler {

    private Hackathon hackathon;

    public void creaHackathon(Utente utente, String nome, InfoHack info) {
        this.hackathon = new Hackathon(info,nome,utente) {
        };
    }

    int giorniScadenzaHackathon = Integer.parseInt(System.getenv("HACKATHON_SCAD_GG"));

    public void setInfo(InfoHack info) {
        hackathon.setInfo(info);
    }

    public void modificaRegolamento(String nuovoRegolamento) {
        hackathon.modificaRegolamento(nuovoRegolamento);
    }

    public void modificaDataInizio(LocalDateTime nuovaData) {
        hackathon.modificaDataInizio(nuovaData);
    }

    public static void aggiungiMentore(Utente mentore, Hackathon hackathon) {
        hackathon.aggiungiMentore(mentore);
    }

    public static void aggiungiGiudice(Utente giudice, Hackathon hackathon) {
        hackathon.aggiungiGiudice(giudice);
    }
    public static void setStaffIncompleto(Hackathon hackathon, StaffIncompleto staffIncompleto) {
        hackathon.setStaffIncompleto(staffIncompleto);
    }

    public void invitaStaff(Utente utente, RuoliStaff tipoRuolo) {
        hackathon.invitaStaff(utente, tipoRuolo);
    }

    public void conferma() {
        hackathon.conferma();
    }

    public void elimina() {
        hackathon.elimina();
    }

    public List<Sottomissione> getSottomissioniValutate() {
        return hackathon.getSottomissioniValutate();
    }

    public List<TeamIscritto> calcolaClassificaPreliminare() {
        return hackathon.calcolaClassificaPreliminare();
    }

    public List<TeamIscritto> visualizzaClassifica() {
        return hackathon.getClassificaCorrente();
    }

    public Sottomissione getDettagli(long sottomissioneID) {
        return hackathon.getDettagliSottomissione(sottomissioneID);
    }

    public String getGiudizio(long sottomissioneID) {
        return hackathon.getGiudizioSottomissione(sottomissioneID);
    }

    public void aggiornaClassifica(List<Long> nuovoOrdineTeam) {
        hackathon.aggiornaClassifica(nuovoOrdineTeam);
    }

    public void confermaClassifica() {
        hackathon.confermaClassifica();
    }

    public void setTeamVincitore(TeamIscritto team) {
        hackathon.setTeamVincitore(team);
    }

    public double getPremioInDenaro() {
        return hackathon.getPremioInDenaro();
    }

    public void concludiHackathon() {
        hackathon.cambiaStato(new ConclusoState());
    }
}
