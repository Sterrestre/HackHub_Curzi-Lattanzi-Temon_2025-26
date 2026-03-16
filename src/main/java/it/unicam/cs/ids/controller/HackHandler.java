package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.*;
import java.time.LocalDateTime;

public class HackHandler {

    private Hackathon hackathon;

    public void creaHackathon(String id, String nome, InfoHack info) {
        this.hackathon = new Hackathon(info) {
        };
    }

    public void setInfo(InfoHack info) {
        hackathon.setInfo(info);
    }

    public void modificaRegolamento(String nuovoRegolamento) {
        hackathon.modificaRegolamento(nuovoRegolamento);
    }

    public void modificaDataInizio(LocalDateTime nuovaData) {
        hackathon.modificaDataInizio(nuovaData);
    }

    public void aggiungiMentore(RuoloPartecipazione mentore) {
        hackathon.aggiungiMentore(mentore);
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
}
