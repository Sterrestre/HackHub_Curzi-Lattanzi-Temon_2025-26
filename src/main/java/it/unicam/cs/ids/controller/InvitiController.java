package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.InvitaStaffRequest;
import it.unicam.cs.ids.dto.InvitaTeamRequest;
import it.unicam.cs.ids.dto.RispostaInvitoRequest;
import it.unicam.cs.ids.handler.InvitiHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.inviti.Invito;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.InvitoService;
import it.unicam.cs.ids.service.UtenteService;
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
    private final InvitoService invitoService;

    public InvitiController(InvitiHandler invitiHandler,
                            UtenteService utenteService,
                            TeamService teamService,
                            HackathonService hackService, InvitoService invitoService) {
        this.invitiHandler = invitiHandler;
        this.utenteService = utenteService;
        this.teamService = teamService;
        this.hackService = hackService;
        this.invitoService = invitoService;
    }

    @PostMapping("/staff")
    public ResponseEntity<String> invitaStaff(@RequestBody InvitaStaffRequest req) {

        Hackathon hack = hackService.getHackathonByID(req.hackathonId());
     //   Utente utente = utenteService.findById(req.utenteId());
     //   Utente organizzatore = utenteService.findById(req.organizzatoreId());

    //    invitiHandler.creaInvitoStaff(organizzatore, utente, hack, req.ruolo());

        return ResponseEntity.ok("Invito staff inviato");
    }

    @PostMapping("/team")
    public ResponseEntity<String> invitaTeam(@RequestBody InvitaTeamRequest req) {

        // MEMBRO TEAM TODO
    //    MembroTeam team = teamService.findById(req.teamId());
    //    Utente utente = utenteService.findById(req.utenteId());

    //    invitiHandler.creaInvitoTeam(team, utente);

        return ResponseEntity.ok("Invito inviato al membro del team");
    }

    @PostMapping("/rispondi")
    public ResponseEntity<String> rispondi(@RequestBody RispostaInvitoRequest req) {
    //    Invito invito = invitoService.findById(req.invitoId());

    //    invitiHandler.rispostaInvito(invito, req.accetta());

        return ResponseEntity.ok("Risposta registrata");
    }
}

