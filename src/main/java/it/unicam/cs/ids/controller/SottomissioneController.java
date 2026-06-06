package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.CreaSottomissioneRequest;
import it.unicam.cs.ids.dto.SottomissioneDTO;
import it.unicam.cs.ids.dto.ValutaSottomissioneRequest;
import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.SottomissioneService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione delle sottomissioni.
 * Parla solo con SottomissioneService e HackathonService.
 * La sottomissione si recupera sempre tramite hackathon → teamIscritto.
 */
@RestController
@RequestMapping("/sottomissioni")
@Transactional
public class SottomissioneController {

    private final SottomissioneService sottomissioneService;
    private final HackathonService hackathonService;

    public SottomissioneController(SottomissioneService sottomissioneService,
                                   HackathonService hackathonService) {
        this.sottomissioneService = sottomissioneService;
        this.hackathonService = hackathonService;
    }

    /**
     * Carica la sottomissione di un team iscritto a un hackathon.
     */
    @PostMapping("/carica")
    public ResponseEntity<?> carica(@RequestBody CreaSottomissioneRequest req) {
        try {
            Sottomissione s = sottomissioneService.caricaSottomissione(
                    req.hackathonId(),
                    req.teamIscrittoId(),
                    req.titolo(),
                    req.descrizione(),
                    req.linkRepository()
            );
            return ResponseEntity.ok(SottomissioneDTO.from(s));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    /**
     * Valuta la sottomissione di un team iscritto.
     */
    @PostMapping("/valuta")
    public ResponseEntity<?> valuta(@RequestBody ValutaSottomissioneRequest req) {
        try {
            Sottomissione s = sottomissioneService.valutaSottomissione(
                    req.giudiceId(),
                    req.hackathonId(),
                    req.teamIscrittoId(),
                    req.voto(),
                    req.giudizio()
            );
            return ResponseEntity.ok(SottomissioneDTO.from(s));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    /**
     * Recupera la sottomissione di un team iscritto tramite hackathon → teamIscritto.
     */
    @GetMapping("/hackathon/{hackathonId}/team/{teamIscrittoId}")
    public ResponseEntity<?> getBySottomissione(@PathVariable String hackathonId,
                                                @PathVariable String teamIscrittoId) {
        try {
            Sottomissione s = sottomissioneService.visualizzaSottomissione(hackathonId, teamIscrittoId);
            return ResponseEntity.ok(SottomissioneDTO.from(s));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Recupera tutte le sottomissioni di un hackathon.
     */
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<SottomissioneDTO>> getByHackathon(@PathVariable String hackathonId) {
        Hackathon hack = hackathonService.getHackathonByID(hackathonId);
        List<SottomissioneDTO> lista = hack.getSottomissioni().stream()
                .map(SottomissioneDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }
}