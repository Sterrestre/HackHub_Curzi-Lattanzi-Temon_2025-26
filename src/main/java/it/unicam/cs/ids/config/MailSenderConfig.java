package it.unicam.cs.ids.config;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.MailSender;
import it.unicam.cs.ids.service.infrastructure.gmail.GmailMailSender;
import it.unicam.cs.ids.service.infrastructure.gmail.MockMailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailSenderConfig {

    @Bean
    @ConditionalOnProperty(name = "gmail.enabled", havingValue = "true")
    public MailSender mailSender(Gmail gmail) {
        return new GmailMailSender(gmail);
    }

    @Bean
    @ConditionalOnProperty(name = "gmail.enabled", havingValue = "false", matchIfMissing = true)
    public MailSender mockMailSender() {
        return new MockMailSender();
    }
}

// TODO : risolvi la questione mail e sblocca l'adapter

