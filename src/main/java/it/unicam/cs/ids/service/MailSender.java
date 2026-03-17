package it.unicam.cs.ids.service;

public interface MailSender {

    /**
     * Invia una email al destinatario specificato.
     *
     * @param destinatario indirizzo email del destinatario
     * @param oggetto oggetto della mail
     * @param corpo corpo della mail
     */
    void inviaEmail(String destinatario, String oggetto, String corpo);
}