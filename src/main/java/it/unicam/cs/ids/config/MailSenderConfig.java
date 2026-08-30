package it.unicam.cs.ids.config;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.MailSender;
import it.unicam.cs.ids.service.infrastructure.gmail.GmailMailSender;
import it.unicam.cs.ids.service.infrastructure.gmail.MockMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MailSenderConfig {

    @Value("${GMAIL_SENDER}")
    private String mittente;


    @Bean
    @Profile("prod")
    public MailSender mailSender(Gmail gmail) {
        return new GmailMailSender(gmail, mittente);
    }

    @Bean
    @Profile("dev")
    public MailSender mockMailSender() {
        return new MockMailSender();
    }
}

