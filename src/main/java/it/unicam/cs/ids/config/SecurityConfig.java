package it.unicam.cs.ids.config;

import it.unicam.cs.ids.handler.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error").permitAll()
                        // TEMPORANEO: apre in lettura tutte le rotte per poter sviluppare
                        // e testare il frontend prima che l'integrazione col login sia pronta.
                        // Da rimuovere quando l'autenticazione sara' collegata all'app Angular.
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/**").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(new OAuth2LoginSuccessHandler())
                );

        return http.build();
    }
}
