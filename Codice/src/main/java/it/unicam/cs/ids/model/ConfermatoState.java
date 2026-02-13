package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

public class ConfermatoState implements HackState {

    public ConfermatoState(Hackathon hackathon) {
    }

    @Override
    public void setInfoHack(Hackathon hackathon, InfoHack info) {
        throw new IllegalStateException("Non puoi modificare info dopo la conferma");
    }

    @Override
    public void modificaRegolamento(Hackathon hackathon, String nuovoRegolamento) {
        throw new IllegalStateException("Regolamento non modificabile in stato confermato");
    }

    @Override
    public void modificaDataInizio(Hackathon hackathon, LocalDateTime nuovaData) {
        throw new IllegalStateException("Data non modificabile in stato confermato");
    }

    @Override
    public void modificaDataFine(Hackathon hackathon, LocalDateTime nuovaDataFine) {
        throw new IllegalStateException("Data non modificabile in stato confermato");
    }

    @Override
    public void modificaLuogo(Hackathon hackathon, String nuovoLuogo) {
        throw new IllegalStateException("Luogo non modificabile in stato confermato");
    }

    @Override
    public void modificaScadenzaIscrizioni(Hackathon hackathon, LocalDateTime nuovaScadenza) {
        throw new IllegalStateException("Scadenza non modificabile in stato confermato");
    }

    @Override
    public void modificaQuotaIscrizione(Hackathon hackathon, double nuovaQuota) {
        throw new IllegalStateException("Quota non modificabile in stato confermato");
    }

    @Override
    public void modificaPremio(Hackathon hackathon, double nuovoPremio) {
        throw new IllegalStateException("Premio non modificabile in stato confermato");
    }

    @Override
    public void modificaDimMaxTeam(Hackathon hackathon, int nuovaDim) {
        throw new IllegalStateException("Dimensione team non modificabile in stato confermato");
    }

    @Override
    public void modificaNumMaxTeam(Hackathon hackathon, int nuovoNum) {
        throw new IllegalStateException("Numero team non modificabile in stato confermato");
    }


    @Override
    public void aggiungiMentore(Hackathon hackathon, RuoloPartecipazione mentore) {
        hackathon.ruoli.add(mentore);
    }

    @Override
    public void invitaStaff(Hackathon hackathon, Organizzatore organizzatore) {
        throw new IllegalStateException("Non puoi invitare staff in stato confermato");
    }

    @Override
    public void eliminaHackathon(Hackathon hackathon) {
        // eliminazione consentita
        // logica da implementare
    }

    @Override
    public void confermaHackathon(Hackathon hackathon) {
        hackathon.CambiaStato(new ConfermatoState(hackathon));{

    }
}
}
