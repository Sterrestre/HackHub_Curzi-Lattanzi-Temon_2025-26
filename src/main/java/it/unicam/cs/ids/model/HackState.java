package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

/**
 * Questa interfaccia permette di alterare il comportamento di una classe Hackathon in modo dinamico,
 * rispetto al suo stato interno.
 * L'interfaccia implementa il pattern State.
 */

public interface HackState {

    void setInfoHack(Hackathon hackathon, InfoHack info);

    void modificaRegolamento(Hackathon hackathon, String nuovoRegolamento);

    void modificaDataInizio(Hackathon hackathon, LocalDateTime nuovaDataInizio);

    void modificaDataFine(Hackathon hackathon, LocalDateTime nuovaDataFine);

    void modificaLuogo(Hackathon hackathon, String nuovoLuogo);

    void modificaScadenzaIscrizioni(Hackathon hackathon, LocalDateTime nuovaScadenza);

    void modificaQuotaIscrizione(Hackathon hackathon, double nuovaQuota);

    void modificaPremio(Hackathon hackathon, double nuovoPremio);

    void modificaDimMaxTeam (Hackathon hackathon, int nuovaDim);

    void modificaNumMaxTeam(Hackathon hackathon, int nuovoNum);

    void aggiungiMentore(Hackathon hackathon, Utente utente);

    void aggiungiGiudice(Hackathon hackathon, Utente utente);

    void invitaStaff(Hackathon hackathon, Utente utente, RuoliStaff tipoRuolo);

    void eliminaHackathon(Hackathon hackathon);

    void confermaHackathon(Hackathon hackathon);

    boolean isStaffIncompleto(Hackathon hackathon);
}
