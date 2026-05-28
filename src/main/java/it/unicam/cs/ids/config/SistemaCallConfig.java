package it.unicam.cs.ids.config;

import it.unicam.cs.ids.service.SistemaCall;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SistemaCallConfig {

    @Bean
    public SistemaCall sistemaCall() {
        return new SistemaCall();
    }
}

