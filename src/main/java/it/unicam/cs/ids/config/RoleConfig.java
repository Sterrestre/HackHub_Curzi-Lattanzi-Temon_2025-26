package it.unicam.cs.ids.config;

import it.unicam.cs.ids.model.staff.RoleFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleConfig {

    @Bean
    public RoleFactory roleFactory() {
        return new RoleFactory();
    }
}

