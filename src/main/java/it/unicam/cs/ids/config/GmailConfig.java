package it.unicam.cs.ids.config;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.infrastructure.gmail.GmailClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GmailConfig {

    @Bean
    public Gmail gmailClient() throws Exception {
        return GmailClientFactory.createGmailClient();
    }
}
