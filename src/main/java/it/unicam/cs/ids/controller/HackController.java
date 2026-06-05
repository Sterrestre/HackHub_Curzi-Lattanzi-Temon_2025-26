package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.CreaHackathonRequest;
import it.unicam.cs.ids.dto.HackathonDTO;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.InfoHack;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione degli hackathon.
 * Parla con HackathonService e UtenteService per recuperare le entità.
 */
@RestController
@RequestMapping("/hackathon")
public class HackController {

    private final HackathonService hackathonService;
    private final UtenteService utenteService;

    public HackController(HackathonService hackathonService, UtenteService utenteService) {
        this.hackathonService = hackathonService;
        this.utenteService = utenteService;
    }

    @PostMapping("/crea")
    public ResponseEntity<?> crea(@RequestBody CreaHackathonRequest req) {
        try {
            Utente organizzatore = utenteService.findById(req.organizzatoreId());

            InfoHack info = new InfoHack.Builder()
                    .regolamento(req.regolamento())
                    .dataInizio(req.dataInizio())
                    .dataFine(req.dataFine())
                    .scadenzaIscrizioni(req.scadenzaIscrizioni())
                    .luogo(req.luogo())
                    .quotaIscrizione(req.quotaIscrizione())
                    .premio(req.premio())
                    .numMaxTeam(req.numMaxTeam())
                    .dimMaxTeam(req.maxPartecipantiPerTeam())
                    .build();

            return ResponseEntity.ok(HackathonDTO.from(
                    hackathonService.creaHackathon(organizzatore, req.nome(), info)
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getHackathon(@PathVariable String id) {
        try {
            return ResponseEntity.ok(HackathonDTO.from(hackathonService.getHackathonByID(id)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<HackathonDTO>> getAll() {
        List<HackathonDTO> lista = hackathonService.getTutti()
                .stream()
                .map(HackathonDTO::from)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{id}/conferma")
    public ResponseEntity<?> conferma(@PathVariable String id) {
        try {
            hackathonService.aggiornaStato(id, Stato.CONFERMATO);
            return ResponseEntity.ok("Hackathon confermato");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/stato")
    public ResponseEntity<?> cambiaStato(@PathVariable String id, @RequestBody Stato nuovoStato) {
        try {
            hackathonService.aggiornaStato(id, nuovoStato);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> elimina(@PathVariable String id) {
        try {
            hackathonService.eliminaHackathon(id);
            return ResponseEntity.ok("Hackathon eliminato");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }
}