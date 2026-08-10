package it.unicam.cs.ids.security;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.service.UtenteService;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.server.ResponseStatusException;

/**
 * Risolve i parametri annotati con @UtenteCorrente, recuperando l'utente
 * autenticato dalla sessione corrente (tramite il suo indirizzo email,
 * fornito da Google via OAuth2) e cercandolo nel database.
 */
@Component
public class UtenteCorrenteArgumentResolver implements HandlerMethodArgumentResolver {

    private final UtenteService utenteService;

    public UtenteCorrenteArgumentResolver(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(UtenteCorrente.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof OAuth2User oauthUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Nessun utente autenticato");
        }

        String email = oauthUser.getAttribute("email");

        try {
            return utenteService.findByEmail(email);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Utente non riconosciuto");
        }
    }
}