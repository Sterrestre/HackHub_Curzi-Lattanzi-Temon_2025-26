package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.repository.UtenteRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private UtenteRepository repo;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String nomeCompleto = oauthUser.getAttribute("name");

        // Divido nome e cognome se possibile
        String nome = nomeCompleto != null ? nomeCompleto.split(" ")[0] : "";
        String cognome = nomeCompleto != null && nomeCompleto.contains(" ")
                ? nomeCompleto.substring(nome.length()).trim()
                : "";

        // Verifico se l'utente esiste già
        Optional<Utente> existing = repo.findByUtenteEmail(email);

        // Se l'utente non esiste, lo creo
        if (existing.isEmpty()) {
            Utente nuovo = new Utente(
                    UUID.randomUUID().toString(), // utenteID
                    nome,                         // utenteNome
                    cognome,                      // utenteCognome
                    email,                        // utenteEmail
                    nome,                         // nickname provvisorio
                    "",                           // biografia vuota
                    LocalDate.now()               // data di nascita placeholder
            );

            repo.save(nuovo);

            // TODO: eventuale redirect ad una pagina di primo accesso
            // response.sendRedirect("profilo/completa");

        // se l'utente esiste, non modifico nulla
        } else {
            Utente utente = existing.get();
            repo.save(utente);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
