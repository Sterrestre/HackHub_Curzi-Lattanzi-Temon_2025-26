package it.unicam.cs.ids.config;

import it.unicam.cs.ids.handler.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OAuth2LoginSuccessHandler successHandler) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/error").permitAll()
                        // Consultare hackathon, classifiche e sottomissioni e' pubblico:
                        // non serve un account per guardare, solo per agire.
                        .requestMatchers(HttpMethod.GET, "/**").permitAll()
                        // Tutto il resto (creare, iscrivere, valutare, ecc.) richiede login.
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .loginPage("/login")
                        .successHandler(successHandler)
                );

        return http.build();
    }
}