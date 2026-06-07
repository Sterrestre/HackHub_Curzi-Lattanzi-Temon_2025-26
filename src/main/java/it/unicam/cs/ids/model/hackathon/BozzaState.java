package it.unicam.cs.ids.model.hackathon;

import it.unicam.cs.ids.handler.InvitiHandler;
import it.unicam.cs.ids.model.staff.RuoliStaff;
import it.unicam.cs.ids.model.staff.RuoloPartecipazione;
import it.unicam.cs.ids.model.Utente;

import java.time.LocalDateTime;

// Pattern: State
public class BozzaState implements HackState {

    protected final InvitiHandler invitiHandler;

    public BozzaState(InvitiHandler invitiHandler) {
        this.invitiHandler = invitiHandler;
    }

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
        hackathon.infoHack.setDataInizio(nuovaData);
    }

    @Override
    public void modificaDataFine(Hackathon hackathon, LocalDateTime nuovaDataFine) {
        hackathon.infoHack.setDataFine(nuovaDataFine);
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
    public void aggiungiGiudice(Hackathon hackathon, RuoloPartecipazione giudice) {
        hackathon.ruoli.add(giudice);
    }

    @Override
    public void aggiungiMentore(Hackathon hackathon, RuoloPartecipazione mentore) {
        hackathon.ruoli.add(mentore);
    }

    @Override
    public void invitaStaff(Hackathon hackathon, Utente utente, RuoliStaff tipoRuolo) {
        // Validazione: verifica che l'utente non sia null
        if (utente == null) {
            throw new IllegalArgumentException("L'utente non può essere null");
        }

        // Validazione: verifica che il tipo ruolo non sia null
        if (tipoRuolo == null) {
            throw new IllegalArgumentException("Il tipo di ruolo non può essere null");
        }

        // Validazione: verifica che l'utente abbia un'email valida
        if (utente.getUtenteEmail() == null || utente.getUtenteEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("L'utente deve avere un'email valida per ricevere l'invito");
        }

        // Validazione: verifica che l'utente non sia già presente nello staff con qualsiasi ruolo
        if (hackathon.getRuoli() != null) {
            for (RuoloPartecipazione ruolo : hackathon.getRuoli()) {
                if (ruolo.getUtente().equals(utente)) {
                    throw new IllegalStateException(
                        "L'utente " + utente.getNickname() +
                        " è già presente nello staff di questo hackathon come " +
                        ruolo.getTipoRuolo()
                    );
                }
            }
        }
        invitiHandler.creaInvitoStaff(hackathon.getOrganizzatore(), utente, hackathon, tipoRuolo);;

    }

    @Override
    public void eliminaHackathon(Hackathon hackathon) {
        // Validazione: verifica che l'hackathon non sia null
        if (hackathon == null) {
            throw new IllegalArgumentException("L'hackathon non può essere null");
        }
        hackathon.elimina();
    }

    @Override
    public void confermaHackathon(Hackathon hackathon) {
        // Validazione: verifica che le informazioni essenziali siano complete
        if (hackathon.getInfoHack() == null) {
            throw new IllegalStateException("Impossibile confermare: informazioni hackathon mancanti");
        }

        InfoHack info = hackathon.getInfoHack();
        if (info.getDataInizio() == null || info.getDataFine() == null) {
            throw new IllegalStateException("Impossibile confermare: date mancanti");
        }

        if (info.getLuogo() == null || info.getLuogo().trim().isEmpty()) {
            throw new IllegalStateException("Impossibile confermare: luogo mancante");
        }

        if (info.getScadenzaIscrizioni() == null) {
            throw new IllegalStateException("Impossibile confermare: scadenza iscrizioni mancante");
        }

        // Validazione: verifica che lo staff sia completo
        if (this.isStaffIncompleto(hackathon)) {
            throw new IllegalStateException("Impossibile confermare: staff incompleto");
        }

        // Cambio stato in confermato
        hackathon.cambiaStato(new ConfermatoState(hackathon));
    }

    @Override
    public boolean isStaffIncompleto(Hackathon hackathon) {
        if (hackathon.getRuoli() == null || hackathon.getRuoli().isEmpty()) {
            return true;
        }

        boolean haMentore = false;
        boolean haGiudice = false;

        for (RuoloPartecipazione ruolo : hackathon.getRuoli()) {
            if (ruolo.getTipoRuolo() == RuoliStaff.MENTORE) {
                haMentore = true;
            } else if (ruolo.getTipoRuolo() == RuoliStaff.GIUDICE) {
                haGiudice = true;
            }
        }

        return !haMentore || !haGiudice;
    }
}
