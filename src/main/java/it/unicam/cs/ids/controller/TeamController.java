package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.*;
import it.unicam.cs.ids.handler.TeamHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.service.UtenteService;
import it.unicam.cs.ids.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller per la gestione dei team.
 * Recupera le entità tramite UtenteService e TeamService,
 * delega la logica di dominio a TeamHandler.
 */
@RestController
@RequestMapping("/team")
@Transactional
public class TeamController {

    private final TeamHandler teamHandler;
    private final UtenteService utenteService;
    private final TeamService teamService;

    public TeamController(TeamHandler teamHandler,
                          UtenteService utenteService,
                          TeamService teamService) {
        this.teamHandler = teamHandler;
        this.utenteService = utenteService;
        this.teamService = teamService;
    }

    @PostMapping("/crea")
    public ResponseEntity<?> creaTeam(@RequestBody CreaTeamRequest request) {
        try {
            Utente admin = utenteService.findById(request.amministratoreId());
            Team team = teamService.creaTeam(request.nome(), admin);
            return ResponseEntity.ok(TeamDTO.from(team));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/aggiungi-membro")
    public ResponseEntity<?> aggiungiMembro(@RequestBody AggiungiMembroRequest request) {
        try {
            Team team = teamService.findTeamById(request.teamId());
            Utente utente = utenteService.findById(request.utenteId());
            MembroTeam membro = teamService.aggiungiMembro(team, utente, request.amministratore());
            return ResponseEntity.ok(MembroTeamDTO.from(membro));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/invita")
    public ResponseEntity<?> invita(@RequestBody InvitaRequest request) {
        try {
            MembroTeam admin = teamService.findMembroTeamById(request.teamId(), request.adminId());
            Utente utente = utenteService.findById(request.utenteId());
            String msg = teamHandler.invitaUtente(admin, utente);
            return ResponseEntity.ok(msg);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }
}