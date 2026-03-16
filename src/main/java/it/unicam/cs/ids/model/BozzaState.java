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

        // Crea il ruolo appropriato usando la factory
        RuoloPartecipazione nuovoRuolo;
        RoleFactory factory = new RoleFactory();

        switch (tipoRuolo) {
            case ORGANIZZATORE:
                nuovoRuolo = factory.assegnaOrganizzatore(utente, hackathon);
                break;
            case GIUDICE:
                nuovoRuolo = factory.assegnaGiudice(utente, hackathon);
                break;
            case MENTORE:
                nuovoRuolo = factory.assegnaMentore(utente, hackathon);
                break;
            default:
                throw new IllegalArgumentException("Tipo di ruolo non supportato: " + tipoRuolo);
        }

        // Aggiunge il ruolo alla lista
        hackathon.ruoli.add(nuovoRuolo);

        // TODO: Implementare l'invio dell'invito tramite InvitoSender
        // InvitoSender sender = new Gmail(); // o altra implementazione
        // Invito invito = new InvitoStaff(utente, hackathon, tipoRuolo);
        // sender.invia(invito);
    }

    @Override
    public void eliminaHackathon(Hackathon hackathon) {
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
