package it.unicam.cs.ids.service.infrastructure.gmail;

import it.unicam.cs.ids.service.MailSender;

public class MockMailSender implements MailSender {

    @Override
    public void inviaEmail(String to, String subject, String body) {
        System.out.println("MOCK EMAIL →");
        System.out.println("To: " + to);
        System.out.println("Subject: " + subject);
        System.out.println("Body: " + body);
        System.out.println("Email NON inviata (modalità sviluppo).");
    }
}

