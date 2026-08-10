package it.unicam.cs.ids.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import it.unicam.cs.ids.dto.UtenteDTO;
import it.unicam.cs.ids.dto.RegistraUtenteRequest;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione degli utenti.
 * Parla solo con UtenteService per recuperare e creare utenti.
 */
@RestController
@RequestMapping("/api/utenti")
@Transactional
public class UtenteController {

    private final UtenteService utenteService;

    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @PostMapping("/registra")
    public ResponseEntity<?> registra(@RequestBody RegistraUtenteRequest req) {
        try {
            Utente utente = utenteService.registra(req);
            return ResponseEntity.ok(UtenteDTO.from(utente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        try {
            Utente utente = utenteService.findById(id);
            return ResponseEntity.ok(UtenteDTO.from(utente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<UtenteDTO>> getAll() {
        List<UtenteDTO> lista = utenteService.findAll().stream()
                .map(UtenteDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/cerca")
    public ResponseEntity<List<UtenteDTO>> cerca(@RequestParam String nickname) {
        List<UtenteDTO> lista = utenteService.cercaPerNickname(nickname).stream()
                .map(UtenteDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUtenteCorrente(
            @AuthenticationPrincipal org.springframework.security.oauth2.core.user.OAuth2User principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("Nessun utente autenticato");
        }
        try {
            String email = principal.getAttribute("email");
            Utente utente = utenteService.findByEmail(email);
            return ResponseEntity.ok(UtenteDTO.from(utente));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logout effettuato");
    }
}