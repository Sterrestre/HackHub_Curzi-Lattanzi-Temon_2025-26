package it.unicam.cs.ids.config;

import it.unicam.cs.ids.security.UtenteCorrenteArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * Configurazione web trasversale: CORS (permette al frontend Angular, su
 * un'origine diversa dal backend, di chiamare le API REST) e la
 * registrazione del resolver per l'annotazione @UtenteCorrente.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final UtenteCorrenteArgumentResolver utenteCorrenteArgumentResolver;

    public CorsConfig(UtenteCorrenteArgumentResolver utenteCorrenteArgumentResolver) {
        this.utenteCorrenteArgumentResolver = utenteCorrenteArgumentResolver;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:4200",   // frontend Angular in sviluppo locale
                        "http://localhost:80",     // frontend servito da Nginx in Docker (locale)
                        "http://localhost",
                        "http://35-181-19-124.sslip.io"     // frontend servito da Nginx su AWS
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(utenteCorrenteArgumentResolver);
    }
}