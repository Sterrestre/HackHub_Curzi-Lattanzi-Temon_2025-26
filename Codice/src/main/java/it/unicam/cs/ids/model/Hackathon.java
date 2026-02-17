package it.unicam.cs.ids.model;

import java.time.LocalDateTime;
import java.util.List;

public abstract class Hackathon {

    public InfoHack InfoHack;
    protected HackState state;              // stato corrente
    protected String hackID;
    protected String nome;
    protected InfoHack infoHack;
    protected Stato stato;                  // enum: BOZZA, CONFERMATO, CONCLUSO
    protected List<RuoloPartecipazione> ruoli;
    protected int numTeamIscritti;

    public Hackathon(InfoHack infoHack) {
        this.hackID = hackID;
        this.infoHack = infoHack;
        this.stato = Stato.BOZZA;
        this.numTeamIscritti = 0;
    }

    public void setState(HackState newState) {
        this.state = newState;
    }


    public void setInfoHack(InfoHack infoHack) {
        this.infoHack = infoHack;
    }

    public void setInfo(InfoHack info) {
        state.setInfoHack(this, info);
    }

    public void modificaRegolamento(String nuovoRegolamento) {
        state.modificaRegolamento(this, nuovoRegolamento);
    }

    public void modificaDataInizio(LocalDateTime nuovaData) {
        state.modificaDataInizio(this, nuovaData);
    }

    public void modificaDataFine(LocalDateTime nuovaData) {
        state.modificaDataFine(this, nuovaData);
    }

    public void modificaLuogo(String nuovoLuogo) {
        state.modificaLuogo(this, nuovoLuogo);
    }

    public void modificaScadenzaIscrizioni(LocalDateTime nuovaScadenza) {
        state.modificaScadenzaIscrizioni(this, nuovaScadenza);
    }

    public void modificaQuotaIscrizione(double nuovaQuota) {
        state.modificaQuotaIscrizione(this, nuovaQuota);
    }

    public void modificaPremio(double nuovoPremio) {
        state.modificaPremio(this, nuovoPremio);
    }

    public void modificaDimMaxTeam(int nuovaDim) {
        state.modificaDimMaxTeam(this, nuovaDim);
    }

    public void modificaNumMaxTeam(int nuovoNum) {
        state.modificaNumMaxTeam(this, nuovoNum);
    }


    public void aggiungiMentore(RuoloPartecipazione mentore) {
        state.aggiungiMentore(this, mentore);
    }

    public void invitaStaff(Utente utente, RuoliStaff tipoRuolo) {
        state.invitaStaff(this, utente, tipoRuolo);
    }

    public void elimina() {
        state.eliminaHackathon(this);
    }

    public void conferma() {
        state.confermaHackathon(this);
    }

    public void cambiaStato(HackState nuovoStato) {
        this.state = nuovoStato;
        // Aggiorna anche l'enum Stato in base al tipo di stato
        if (nuovoStato instanceof BozzaState) {
            this.stato = Stato.BOZZA;
        } else if (nuovoStato instanceof ConfermatoState) {
            this.stato = Stato.CONFERMATO;
        } else if (nuovoStato instanceof ConclusoState) {
            this.stato = Stato.CONCLUSO;
        }
    }

    public InfoHack getInfoHack() {
        return this.infoHack;
    }

    public List<RuoloPartecipazione> getRuoli() {
        return this.ruoli;
    }
}
