package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.InvitaStaffRequest;
import it.unicam.cs.ids.dto.InvitaTeamRequest;
import it.unicam.cs.ids.dto.RispostaInvitoRequest;
import it.unicam.cs.ids.handler.InvitiHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.team.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inviti")
public class InvitiController {

    // TODO revisionare il controller

    private final InvitiHandler invitiHandler;
    private final UtenteService utenteService;
    private final TeamService teamService;
    private final HackathonService hackService;

    public InvitiController(InvitiHandler invitiHandler,
                            UtenteService utenteService,
                            TeamService teamService,
                            HackathonService hackService) {
        this.invitiHandler = invitiHandler;
        this.utenteService = utenteService;
        this.teamService = teamService;
        this.hackService = hackService;
    }

    @PostMapping("/staff")
    public ResponseEntity<String> invitaStaff(@RequestBody InvitaStaffRequest req) {

        Hackathon hack = hackService.findById(req.hackathonId());
        Utente utente = utenteService.findById(req.utenteId());

        invitiHandler.invitaStaff(hack, utente, req.ruolo());

        return ResponseEntity.ok("Invito staff inviato");
    }

    @PostMapping("/team")
    public ResponseEntity<String> invitaTeam(@RequestBody InvitaTeamRequest req) {

        Team team = teamService.findById(req.teamId());
        Utente utente = utenteService.findById(req.utenteId());

        invitiHandler.invitaTeam(team, utente);

        return ResponseEntity.ok("Invito inviato al membro del team");
    }

    @PostMapping("/rispondi")
    public ResponseEntity<String> rispondi(@RequestBody RispostaInvitoRequest req) {

        invitiHandler.rispondiInvito(req.invitoId(), req.accetta());

        return ResponseEntity.ok("Risposta registrata");
    }
}

