package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.RichiestaSupporto;
import it.unicam.cs.ids.service.MailSender;
import it.unicam.cs.ids.service.sistemaCall;

import java.time.LocalDateTime;

/**
 * Gestisce le operazioni legate alle richieste di supporto e alle call.
 */
public class SupportoHandler {

    private final sistemaCall sistemaCall;

    public SupportoHandler(sistemaCall sistemaCall) {
        this.sistemaCall = sistemaCall;
    }

    /**
     * Genera un collegamento tramite il sistema esterno (Calendar)
     */
    public String generaCollegamento(sistemaCall sistemaCall) {
        return sistemaCall.generaCollegamento(LocalDateTime.now());
    }


    /**
     * Risponde a una richiesta di supporto
     */
    public void rispondiAllaRichiesta(RichiestaSupporto richiesta) {
        System.out.println("Risposta alla richiesta del team: " + richiesta.getRichiesta());
    }


    /**
     * Invia una proposta di call al team
     * DA CONTROLLARE SE NECESSARIO
     */
   // public void inviaPropostaCall(RichiestaSupporto richiesta, LocalDateTime dataOra) {

        // 1. genera link call
      //  String link = sistemaCall.generaCollegamento(dataOra);

   // }


    /**
     * Invia una proposta di call al team
     */
   public void richiestaProponiCall(LocalDateTime dataOra, RichiestaSupporto richiesta, LocalDateTime fineHackathon) {
    if (dataOra == null || fineHackathon == null || !dataOra.isBefore(fineHackathon)) {
        throw new IllegalArgumentException(
                "La data e ora della call devono essere prima della fine dell'hackathon."
        );
    }

    String link = sistemaCall.generaCollegamento(dataOra);
    MailSender.inviaPropostaTeam(richiesta, dataOra, link);// simula l'invio con la mail

}

    /**
     * Annulla proposta call
     */
    public void annullaCall() {
        System.out.println("Proposta call annullata.");
    }

    /**
     * Verifica che la data sia valida
     */
    public boolean inviaDatiCall(LocalDateTime dataOra, LocalDateTime fineHackathon) {
        return dataOra.isBefore(fineHackathon);
    }
}


