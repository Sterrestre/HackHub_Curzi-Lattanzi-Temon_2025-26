package it.unicam.cs.ids.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotazione da usare sui parametri dei metodi dei controller per ottenere
 * automaticamente l'utente attualmente autenticato (dedotto dalla sessione).
 *
 * Esempio:
 *   public ResponseEntity<?> crea(@RequestBody CreaHackathonRequest req,
 *                                  @UtenteCorrente Utente organizzatore) { ... }
 *
 * Se nessun utente e' autenticato, la richiesta viene bloccata con 401
 * prima ancora che il metodo del controller venga eseguito.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface UtenteCorrente {
}