package it.unicam.cs.ids.model.hackathon;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.staff.RuoliStaff;
import it.unicam.cs.ids.model.staff.RuoloPartecipazione;

import java.time.LocalDateTime;

// Pattern: State
public class InCorsoState implements HackState {

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
    public void modificaDataFine(Hackathon hackathon, LocalDateTime nuovaDataFine) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void modificaLuogo(Hackathon hackathon, String nuovoLuogo) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void modificaScadenzaIscrizioni(Hackathon hackathon, LocalDateTime nuovaScadenza) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void modificaQuotaIscrizione(Hackathon hackathon, double nuovaQuota) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void modificaPremio(Hackathon hackathon, double nuovoPremio) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void modificaDimMaxTeam(Hackathon hackathon, int nuovaDim) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void modificaNumMaxTeam(Hackathon hackathon, int nuovoNum) {
        throw new IllegalStateException("Hackathon in corso");
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, RuoloPartecipazione mentore) {
        throw new IllegalStateException("Hackathon in corso: non modificabile");
    }

    @Override
    public void aggiungiGiudice(Hackathon hackathon, RuoloPartecipazione giudice) {
        throw new IllegalStateException("Hackathon in corso: non modificabile");
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

    @Override
    public boolean isStaffIncompleto(Hackathon hackathon) {
        return false;
    }

}
