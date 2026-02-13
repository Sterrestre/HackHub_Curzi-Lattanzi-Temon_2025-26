package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

public abstract class ConclusoState implements HackState {

    @Override
    public void setInfoHack(Hackathon hackathon, InfoHack info) {
        throw new IllegalStateException("Hackathon concluso: non modificabile");
    }

    @Override
    public void modificaRegolamento(Hackathon hackathon, String nuovoRegolamento) {
        throw new IllegalStateException("Hackathon concluso");
    }

    @Override
    public void modificaDataInizio(Hackathon hackathon, LocalDateTime nuovaData) {
        throw new IllegalStateException("Hackathon concluso");
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, RuoloPartecipazione mentore) {
        throw new IllegalStateException("Hackathon concluso");
    }

    @Override
    public void invitaStaff(Hackathon hackathon, Organizzatore organizzatore) {
        throw new IllegalStateException("Hackathon concluso");
    }

    @Override
    public void eliminaHackathon(Hackathon hackathon) {
        throw new IllegalStateException("Non puoi eliminare un hackathon concluso");
    }

    @Override
    public void confermaHackathon(Hackathon hackathon) {
        throw new IllegalStateException("Hackathon già concluso");
    }
}
