package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

/**
 * Rappresenta lo stato di un hackathon.
 * Questa interfaccia può essere implementata per definire i vari stati
 * che un hackathon può assumere durante il suo ciclo di vita("bozza", "confermato", "concluso").
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

    void aggiungiMentore(Hackathon hackathon, RuoloPartecipazione mentore);

    void invitaStaff(Hackathon hackathon, Utente utente, RuoliStaff tipoRuolo);

    void eliminaHackathon(Hackathon hackathon);

    void confermaHackathon(Hackathon hackathon);

    boolean isStaffIncompleto(Hackathon hackathon);
}
