package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.*;
import it.unicam.cs.ids.handler.TeamHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.service.team.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public class TeamController {

    // TODO Implementa UtenteService e rivedi questa classe
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
    public ResponseEntity<TeamDTO> creaTeam(@RequestBody CreaTeamRequest request) {

        // TODO nota Matteo
        // CONTROLLO SU FIND BY ID (se passa null esplode) <-- controlla nel service che non restituisca null
        Utente admin = utenteService.findById(request.amministratoreId());
        Team team = teamHandler.creaTeam(request.nome(), admin);

        return ResponseEntity.ok(TeamDTO.from(team));
    }

    @PostMapping("/aggiungi-membro")
    public ResponseEntity<MembroTeamDTO> aggiungiMembro(@RequestBody AggiungiMembroRequest request) {

        Team team = teamService.findById(request.teamId());
        Utente utente = utenteService.findById(request.utenteId());

        MembroTeam membro = teamHandler.aggiungiMembroTeam(team, utente, request.amministratore());

        return ResponseEntity.ok(MembroTeamDTO.from(membro));
    }

    @PostMapping("/invita")
    public ResponseEntity<String> invita(@RequestBody InvitaRequest request) {

        MembroTeam admin = teamService.findMembroById(request.adminId());
        Utente utente = utenteService.findById(request.utenteId());

        String msg = teamHandler.invitaUtente(admin, utente);

        return ResponseEntity.ok(msg);
    }
}

