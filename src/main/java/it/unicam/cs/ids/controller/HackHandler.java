package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.*;
import java.time.LocalDateTime;


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
}
