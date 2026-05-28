package it.unicam.cs.ids.config;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.MailSender;
import it.unicam.cs.ids.service.infrastructure.gmail.GmailMailSender;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MailSenderConfig {

    @Bean
    public MailSender mailSender(Gmail gmail) {
        return new GmailMailSender(gmail);
    }
}

