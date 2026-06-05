package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.ProponiCallRequest;
import it.unicam.cs.ids.dto.RichiestaSupportoDTO;
import it.unicam.cs.ids.dto.RispondiSupportoRequest;
import it.unicam.cs.ids.dto.TeamAssegnatoDTO;
import it.unicam.cs.ids.handler.SupportoHandler;
import it.unicam.cs.ids.model.RichiestaSupporto;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.RichiestaSupportoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione del supporto ai team durante un hackathon.
 * Parla con RichiestaSupportoService e HackathonService per recuperare le entità,
 * e con SupportoHandler per la logica di dominio.
 */
@RestController
@RequestMapping("/supporto")
public class SupportoController {

    private final SupportoHandler supportoHandler;
    private final RichiestaSupportoService richiestaSupportoService;
    private final HackathonService hackathonService;

    public SupportoController(SupportoHandler supportoHandler,
                              RichiestaSupportoService richiestaSupportoService,
                              HackathonService hackathonService) {
        this.supportoHandler = supportoHandler;
        this.richiestaSupportoService = richiestaSupportoService;
        this.hackathonService = hackathonService;
    }

    @GetMapping("/genera-link")
    public ResponseEntity<String> generaLink() {
        String link = supportoHandler.generaCollegamento(supportoHandler.getSistemaCall());
        return ResponseEntity.ok(link);
    }

    @PostMapping("/rispondi")
    public ResponseEntity<String> rispondi(@RequestBody RispondiSupportoRequest req) {
        try {
            RichiestaSupporto richiesta = richiestaSupportoService.findById(req.richiestaId());
            supportoHandler.rispondiAllaRichiesta(richiesta);
            return ResponseEntity.ok("Richiesta di supporto risolta");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/proponi-call")
    public ResponseEntity<String> proponiCall(@RequestBody ProponiCallRequest req) {
        try {
            RichiestaSupporto richiesta = richiestaSupportoService.findById(req.richiestaId());
            Hackathon hack = hackathonService.getHackathonByID(richiesta.getHackathonId());
            supportoHandler.richiestaProponiCall(
                    req.dataOra(),
                    richiesta,
                    hack.getInfoHack().getDataFine()
            );
            return ResponseEntity.ok("Proposta di call inviata");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/annulla-call")
    public ResponseEntity<String> annullaCall() {
        try {
            supportoHandler.annullaCall();
            return ResponseEntity.ok("Call annullata");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @GetMapping("/team-assegnati/{hackathonId}/{mentoreId}")
    public ResponseEntity<List<TeamAssegnatoDTO>> getTeamAssegnati(
            @PathVariable String hackathonId,
            @PathVariable String mentoreId) {

        List<TeamAssegnatoDTO> lista = supportoHandler.getTeamAssegnati(hackathonId, mentoreId)
                .stream()
                .map(TeamAssegnatoDTO::from)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/richieste/{hackathonId}/{teamId}")
    public ResponseEntity<List<RichiestaSupportoDTO>> getRichieste(
            @PathVariable String hackathonId,
            @PathVariable String teamId) {

        List<RichiestaSupportoDTO> lista = supportoHandler.getRichiesteSupporto(teamId, hackathonId)
                .stream()
                .map(RichiestaSupportoDTO::from)
                .toList();

        return ResponseEntity.ok(lista);
    }
}