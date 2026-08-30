package it.unicam.cs.ids.config;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.MailSender;
import it.unicam.cs.ids.service.infrastructure.gmail.GmailMailSender;
import it.unicam.cs.ids.service.infrastructure.gmail.MockMailSender;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class MailSenderConfig {

// Da inserire per usare un mittente configurabile tramite application.properties o variabile d'ambiente.
//    @Value("${GMAIL_SENDER}")
//    private String mittente;


    @Bean
    @ConditionalOnProperty(name = "gmail.enabled", havingValue = "true")
// Profili "prod" per usare GmailMailSender in produzione, e "dev" per usare MockMailSender in sviluppo. Non sono attivi a causa di Gmail API, che richiede un URI https.
//    @Profile("prod")
    public MailSender mailSender(Gmail gmail) {
//        return new GmailMailSender(gmail, mittente);
        return new GmailMailSender(gmail);
    }

    @Bean
    @ConditionalOnProperty(name = "gmail.enabled", havingValue = "false")
//    @Profile("dev")
    public MailSender mockMailSender() {
        return new MockMailSender();
    }
}

