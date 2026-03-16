package it.unicam.cs.ids.infrastructure;

public class GmailApiClient implements it.unicam.cs.ids.infrastructure.Gmail {

    private final com.google.api.services.gmail.Gmail gmailService;

    public GmailApiClient(com.google.api.services.gmail.Gmail gmailService) {
        this.gmailService = gmailService;
    }

    @Override
    public void inviaEmail(String destinatario, String oggetto, String corpo) {
        //TODO Implementa invio email usando Gmail API
    }
}
