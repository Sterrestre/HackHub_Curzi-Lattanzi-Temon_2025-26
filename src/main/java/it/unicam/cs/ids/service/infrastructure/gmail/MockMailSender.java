package it.unicam.cs.ids.service.infrastructure.gmail;

import it.unicam.cs.ids.service.MailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

// Pattern: Adapter (Mock)

@ConditionalOnProperty(name = "gmail.enabled", havingValue = "false", matchIfMissing = true)
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

