package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.Penalizzazione;
import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.ConclusoState;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.InfoHack;
import it.unicam.cs.ids.model.hackathon.StaffIncompleto;
import it.unicam.cs.ids.model.staff.Mentore;
import it.unicam.cs.ids.model.staff.RoleFactory;
import it.unicam.cs.ids.model.staff.RuoliStaff;
import it.unicam.cs.ids.model.staff.RuoloPartecipazione;
import it.unicam.cs.ids.model.team.*;

import java.time.LocalDateTime;
import java.util.List;

import static it.unicam.cs.ids.model.staff.RuoliStaff.ORGANIZZATORE;


public class HackHandler {

    private final RoleFactory roleFactory;

    public  HackHandler(RoleFactory roleFactory) {
        this.roleFactory = roleFactory;
    }

    private Hackathon hackathon;

    public void creaHackathon(Utente utente, String nome, InfoHack info) {
        this.hackathon = new Hackathon(info,nome);
        RuoloPartecipazione ruolo = roleFactory.creaERegistraRuolo(ORGANIZZATORE, utente, this.hackathon);
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

    public void aggiungiMentore(Utente mentore, Hackathon hackathon) {
        hackathon.aggiungiMentore(mentore);
    }

    public void aggiungiGiudice(Utente giudice, Hackathon hackathon) {
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

    public List<TeamIscritto> getTeamIscritti() {
        return hackathon.getTeamIscritti();
    }

    public List<MembroTeamIscritto> getMembriIscritti(TeamIscritto team) {
        return team.getElencoIscritti();
    }

    public void validaPresenze() {
        // delega a Hackathon
        hackathon.validaPresenze();
    }

    public void salvaPresenze() {
        hackathon.salvaPresenze();
    }

    public void assegnaMentore(TeamIscritto team, Mentore mentore) {
        team.getElencoIscritti().stream()
                .filter(m -> m.getUtente().equals(mentore))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Mentore non trovato nel team"))
                .setMentoreAssegnato(mentore);

        // TODO setMentoreAssegnato è solo qua. Finire di implementare, inserendo il team nella lista del mentore, e il mentore nel TeamIscritto
    }

    public void impostaPresenza(MembroTeamIscritto membro, boolean presenza) {
        membro.setPresenza(presenza);
    }

    public void setPresente(MembroTeamIscritto membro, boolean presente) {
        membro.setPresenza(presente);
    }

    public Penalizzazione penalizza(TeamIscritto team, String tipoIntervento, String motivazione) {
        // da implementare
        return null;
    }

    public List<TeamIscritto> getTeamIscritti(long hackID) {
        return hackathon.getTeamIscritti();
    }

    public boolean validaDati(String teamID, String tipoIntervento, String motivazione) {

        boolean teamEsiste = hackathon.getTeamIscritti().stream()
                .anyMatch(t -> t.getTeam().getTeamID().equals(teamID));

        if (!teamEsiste) return false;
        if (tipoIntervento == null || tipoIntervento.isBlank()) return false;
        if (motivazione == null || motivazione.isBlank()) return false;

        return true;
    }

    public void applicaPenalizzazione(long teamID, String tipoIntervento, String motivazione) {
        hackathon.applicaPenalizzazione(teamID, tipoIntervento, motivazione);
    }

    // Implementazione del CU "Iscriviti" - It2
    public String iscriviMembroteam(MembroTeam membTeam, Hackathon hackathon){

        // Controllo che la data di scadenza delle iscrizioni non sia stata superata
        LocalDateTime scad = hackathon.getInfoHack().getScadenzaIscrizioni();
        if (scad.isBefore(LocalDateTime.now())) {
            return "Iscrizioni chiuse";
        }

        // Controllo che il num max di iscritti per quel team non sia già stato raggiunto
        int dimMax = hackathon.getInfoHack().getDimMaxTeam();
        Team team = membTeam.getTeam();
        int iscritti = hackathon.getTeamIscritti()                .stream()
                .filter(t -> t.getTeam().equals(team))
                .mapToInt(TeamIscritto::getNumIscritti)
                .sum();
        if (iscritti >= dimMax) {
            return "Numero massimo di iscritti per il team raggiunto";
        }

        // Controllo che non sia un mentore
        if (membTeam.getUtente().getRuoli().stream().anyMatch(r -> r.getHackathon() == hackathon && r.getTipoRuolo() == RuoliStaff.MENTORE)) {
            return "I mentori non possono iscriversi come membri del team";
        }

        // Delego la logica di iscrizione al service
        TeamHandler.iscriviMembroTeam(membTeam, hackathon);
        return "Iscrizione avvenuta con successo";
    }

}
