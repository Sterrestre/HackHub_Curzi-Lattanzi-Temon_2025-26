package it.unicam.cs.ids.model.hackathon;

import it.unicam.cs.ids.model.staff.RuoliStaff;
import it.unicam.cs.ids.model.staff.RuoloPartecipazione;
import it.unicam.cs.ids.model.Utente;

import java.time.LocalDateTime;

public class ConfermatoState implements HackState {

    public ConfermatoState(Hackathon hackathon) {
        if (hackathon == null) {
            throw new IllegalArgumentException("L'hackathon non può essere null");
        }
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
    public void aggiungiMentore(Hackathon hackathon, Utente utente) {
        // TODO

    }

    @Override
    public void aggiungiGiudice(Hackathon hackathon, Utente utente) {
        // TODO
    }


    @Override
    public void aggiungiMentore(Hackathon hackathon, RuoloPartecipazione mentore) {
        hackathon.ruoli.add(mentore);
    }

    @Override
    public void invitaStaff(Hackathon hackathon, Utente utente, RuoliStaff tipoRuolo) {
        throw new IllegalStateException("Non puoi invitare staff in stato confermato");
    }

    @Override
    public void eliminaHackathon(Hackathon hackathon) {
        if (hackathon == null) {
            throw new IllegalArgumentException("L'hackathon non può essere null");
        }
/*
        hackathon.getRuoli().clear();
        hackathon.teamIscritti.clear();
        hackathon.classifica.clear();
        hackathon.sottomissioni.clear();
        hackathon.penalizzazioni.clear();
        hackathon.setInfoHack(null);
        hackathon.setStaffIncompleto(null);
        hackathon.teamVincitore = null;
        hackathon.numTeamIscritti = 0;
        hackathon.classificaConfermata = false;
        hackathon.conto = null;
        hackathon.stato = null;
        hackathon.setState(null);*/
    }

    @Override
    public void confermaHackathon(Hackathon hackathon) {
        throw new IllegalStateException("L'hackathon è già confermato");
    }

    @Override
    public boolean isStaffIncompleto(Hackathon hackathon) {
        return false;
    }
}
