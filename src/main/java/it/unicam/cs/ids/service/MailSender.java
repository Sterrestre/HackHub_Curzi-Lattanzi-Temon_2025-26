package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.RichiestaSupporto;

import java.time.LocalDateTime;

public interface MailSender {

    /**
     * Invia una email al destinatario specificato.
     *
     * @param destinatario indirizzo email del destinatario
     * @param oggetto oggetto della mail
     * @param corpo corpo della mail
     */
    void inviaEmail(String destinatario, String oggetto, String corpo);


    /**
     * Simula invio email al team
     */
    private void inviaPropostaTeam(RichiestaSupporto richiesta, LocalDateTime dataOra, String collegamento) {
        System.out.println("Invio proposta call al team:");
        System.out.println("Data: " + dataOra);
        System.out.println("Link: " + collegamento);
        System.out.println("Messaggio: proposta call per la richiesta -> " + richiesta.getDettagli());
    }

}