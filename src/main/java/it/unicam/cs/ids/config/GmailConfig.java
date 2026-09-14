package it.unicam.cs.ids.config;

import com.google.api.services.gmail.Gmail;
import it.unicam.cs.ids.service.infrastructure.gmail.GmailClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "gmail.enabled", havingValue = "true")
public class GmailConfig {

    @Value("${APP_NAME:HackHub}")
    private String appName;

    @Bean
    @ConditionalOnProperty(name = "gmail.enabled", havingValue = "true")
    public Gmail gmailClient() throws Exception {
        return GmailClientFactory.createGmailClient(appName);
    }
}
