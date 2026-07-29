package it.unicam.cs.ids.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configurazione CORS: permette al frontend Angular (che gira su un'origine diversa,
 * es. localhost:4200 in sviluppo) di chiamare le API REST esposte da questo backend.
 * Senza questa configurazione il browser blocca le richieste per motivi di sicurezza.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:4200",   // frontend Angular in sviluppo locale
                        "http://localhost:80",     // frontend servito da Nginx in Docker
                        "http://localhost"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }
}