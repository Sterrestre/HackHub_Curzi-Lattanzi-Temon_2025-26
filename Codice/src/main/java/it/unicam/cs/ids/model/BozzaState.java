package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

public class BozzaState implements HackState {

    @Override
    public void setInfoHack(Hackathon hackathon, InfoHack info) {
        hackathon.setInfoHack(info);
    }

    @Override
    public void modificaRegolamento(Hackathon hackathon, String nuovoRegolamento) {
        hackathon.infoHack.setRegolamento(nuovoRegolamento);
    }

    @Override
    public void modificaDataInizio(Hackathon hackathon, LocalDateTime nuovaData) {
        hackathon.infoHack.setDataInizio(nuovaData.toLocalDate());
    }

    @Override
    public void modificaDataFine(Hackathon hackathon, LocalDateTime nuovaDataFine) {
        hackathon.infoHack.setDataFine(nuovaDataFine.toLocalDate());
    }

    @Override
    public void modificaLuogo(Hackathon hackathon, String nuovoLuogo) {
        hackathon.infoHack.setLuogo(nuovoLuogo);
    }

    @Override
    public void modificaScadenzaIscrizioni(Hackathon hackathon, LocalDateTime nuovaScadenza) {
        hackathon.infoHack.setScadenzaIscrizioni(nuovaScadenza);
    }

    @Override
    public void modificaQuotaIscrizione(Hackathon hackathon, double nuovaQuota) {
        hackathon.infoHack.setQuotaIscrizione(nuovaQuota);
    }

    @Override
    public void modificaPremio(Hackathon hackathon, double nuovoPremio) {
        hackathon.infoHack.setPremio(nuovoPremio);
    }

    @Override
    public void modificaDimMaxTeam(Hackathon hackathon, int nuovaDim) {
        hackathon.infoHack.setDimMaxTeam(nuovaDim);
    }

    @Override
    public void modificaNumMaxTeam(Hackathon hackathon, int nuovoNum) {
        hackathon.infoHack.setNumMaxTeam(nuovoNum);
    }


    @Override
    public void aggiungiMentore(Hackathon hackathon, RuoloPartecipazione mentore) {
        hackathon.ruoli.add(mentore);
    }

    @Override
    public void invitaStaff(Hackathon hackathon, Organizzatore organizzatore) { // da rivedere
        hackathon.ruoli.add(organizzatore);
    }

    @Override
    public void eliminaHackathon(Hackathon hackathon) {
    }

    @Override
    public void confermaHackathon(Hackathon hackathon) { // da rivedere
        hackathon.CambiaStato(new ConfermatoState(hackathon));
    }

    @Override
    public boolean isStaffIncompleto(Hackathon hackathon) {
        return false;
    }
}
