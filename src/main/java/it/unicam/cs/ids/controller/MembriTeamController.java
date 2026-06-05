package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.CambiaRuoloRequest;
import it.unicam.cs.ids.dto.LasciaTeamRequest;
import it.unicam.cs.ids.dto.MembroTeamDTO;
import it.unicam.cs.ids.handler.MembriTeamHandler;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.service.TeamService;
import it.unicam.cs.ids.service.UtenteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione dei membri dei team.
 * Recupera le entità tramite TeamService e delega la logica a MembriTeamHandler.
 */
@RestController
@RequestMapping("/membri")
public class MembriTeamController {

    private final MembriTeamHandler membriTeamHandler;
    private final TeamService teamService;
    private final UtenteService utenteService;

    public MembriTeamController(MembriTeamHandler membriTeamHandler,
                                TeamService teamService,
                                UtenteService utenteService) {
        this.membriTeamHandler = membriTeamHandler;
        this.teamService = teamService;
        this.utenteService = utenteService;
    }

    @PostMapping("/lascia")
    public ResponseEntity<String> lasciaTeam(@RequestBody LasciaTeamRequest request) {
        try {
            MembroTeam membro = teamService.findMembroTeamById(request.teamId(), request.membroId());
            membriTeamHandler.rimuoviMembroTeam(membro);
            return ResponseEntity.ok("Il membro ha lasciato il team");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/cambia-ruolo")
    public ResponseEntity<MembroTeamDTO> cambiaRuolo(@RequestBody CambiaRuoloRequest request) {
        try {
            MembroTeam admin = teamService.findMembroTeamById(request.teamId(), request.adminId());
            MembroTeam membro = teamService.findMembroTeamById(request.teamId(), request.membroId());
            MembroTeam aggiornato = membriTeamHandler.rendiAmministratore(admin, membro);
            return ResponseEntity.ok(MembroTeamDTO.from(aggiornato));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<MembroTeamDTO>> getMembriTeam(@PathVariable String teamId) {
        Team team = teamService.findTeamById(teamId);
        List<MembroTeamDTO> membri = team.getMembri().stream()
                .map(MembroTeamDTO::from)
                .toList();
        return ResponseEntity.ok(membri);
    }

    @GetMapping("/{teamId}/{membroId}")
    public ResponseEntity<MembroTeamDTO> getMembro(@PathVariable String teamId,
                                                   @PathVariable String membroId) {
        try {
            MembroTeam membro = teamService.findMembroTeamById(teamId, membroId);
            return ResponseEntity.ok(MembroTeamDTO.from(membro));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}