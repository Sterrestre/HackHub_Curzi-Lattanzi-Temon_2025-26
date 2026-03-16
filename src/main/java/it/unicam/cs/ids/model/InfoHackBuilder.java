package it.unicam.cs.ids.model;

import java.time.LocalDateTime;

/**
 * Interfaccia Builder per configurare le informazioni di un hackathon.
 * Le implementazioni forniranno i valori per regolamento, date, luogo,
 * scadenza delle iscrizioni, quota di iscrizione, premio e limiti dei team.
 */
public interface InfoHackBuilder {
    /**
     * Imposta il regolamento dell'evento.
     *
     * @param regolamento testo o riferimento al regolamento dell'hackathon
     */
    void setRegolamento(String regolamento);

    /**
     * Imposta la data e ora di inizio dell'evento.
     *
     * @param data data e ora di inizio (LocalDateTime)
     */
    void setDataInizio(LocalDateTime data);

    /**
     * Imposta la data e ora di fine dell'evento.
     *
     * @param data data e ora di fine (LocalDateTime)
     */
    void setDataFine(LocalDateTime data);

    /**
     * Imposta il luogo in cui si svolgerà l'evento.
     *
     * @param luogo descrizione del luogo o indirizzo
     */
    void setLuogo(String luogo);

    /**
     * Imposta la scadenza per le iscrizioni all'evento.
     *
     * @param scadenzaIscrizioni data e ora limite per le iscrizioni (LocalDateTime)
     */
    void setScadenzaIscrizioni(LocalDateTime scadenzaIscrizioni);

    /**
     * Imposta la quota di iscrizione per team.
     *
     * @param quotaIscrizione importo della quota
     */
    void setQuotaIscrizione(double quotaIscrizione);

    /**
     * Imposta il valore del premio destinato al team vincitore.
     *
     * @param premio ammontare del premio
     */
    void setPremio(double premio);

    /**
     * Imposta la dimensione massima consentita per ogni team.
     *
     * @param dimMaxTeam numero massimo di membri per team
     */
    void setDimMaxTeam(int dimMaxTeam);

    /**
     * Imposta il numero massimo di team ammessi all'evento.
     *
     * @param numMaxTeam numero massimo di team partecipanti
     */
    void setNumMaxTeam(int numMaxTeam);

}
