package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

public abstract class InCorsoState implements HackState {

    @Override
    public void setInfoHack(Hackathon hackathon, InfoHack info) {
        throw new IllegalStateException("Hackathon in corso: non modificabile");
    }

    @Override
    public void modificaRegolamento(Hackathon hackathon, String nuovoRegolamento) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void modificaDataInizio(Hackathon hackathon, LocalDateTime nuovaData) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, RuoloPartecipazione mentore) {
        throw new IllegalStateException("Hackathon in corso");
    }

    public void aggiungiGiudice(Hackathon hackathon, Giudice giudice) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void invitaStaff(Hackathon hackathon, Utente utente, RuoliStaff tipoRuolo) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void eliminaHackathon(Hackathon hackathon) {
        throw new IllegalStateException("Non puoi eliminare un hackathon ancora in corso");
    }

    @Override
    public void confermaHackathon(Hackathon hackathon) {
        throw new IllegalStateException("Hackathon ancora in corso");
    }

}
