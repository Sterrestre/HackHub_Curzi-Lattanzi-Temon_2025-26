package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.InvitaStaffRequest;
import it.unicam.cs.ids.dto.InvitaTeamRequest;
import it.unicam.cs.ids.dto.RispostaInvitoRequest;
import it.unicam.cs.ids.handler.InvitiHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.inviti.Invito;
import it.unicam.cs.ids.model.inviti.InvitoTeam;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.InvitoService;
import it.unicam.cs.ids.service.UtenteService;
import it.unicam.cs.ids.service.TeamService;
import org.springframework.http.ResponseEntity;
import it.unicam.cs.ids.model.inviti.InvitoHackathon;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * Controller per la gestione degli inviti staff e team.
 * Recupera le entità tramite i service e delega la logica a InvitiHandler.
 */
@RestController
@RequestMapping("/inviti")
@Transactional
public class InvitiController {

    private final InvitiHandler invitiHandler;
    private final UtenteService utenteService;
    private final TeamService teamService;
    private final HackathonService hackService;
    private final InvitoService invitoService;

    public InvitiController(InvitiHandler invitiHandler,
                            UtenteService utenteService,
                            TeamService teamService,
                            HackathonService hackService,
                            InvitoService invitoService) {
        this.invitiHandler = invitiHandler;
        this.utenteService = utenteService;
        this.teamService = teamService;
        this.hackService = hackService;
        this.invitoService = invitoService;
    }

    @PostMapping("/staff")
    public ResponseEntity<String> invitaStaff(@RequestBody InvitaStaffRequest req) {
        try {
            Hackathon hack = hackService.getHackathonByID(req.hackathonId());
            Utente utente = utenteService.findById(req.utenteId());
            Utente organizzatore = utenteService.findById(req.organizzatoreId());

            if (!hack.getOrganizzatore().getUtenteID().equals(organizzatore.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può invitare staff");
            }

            // Controllo che l'utente non sia già membro dello staff
            boolean giaStaff = hack.getRuoli().stream()
                    .anyMatch(r -> r.getUtente().getUtenteID().equals(utente.getUtenteID()));
            if (giaStaff) {
                return ResponseEntity.badRequest().body("Questo utente è già membro dello staff di questo hackathon");
            }

            invitiHandler.creaInvitoStaff(organizzatore, utente, hack, req.ruolo());
            return ResponseEntity.ok("Invito staff inviato");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/team")
    public ResponseEntity<String> invitaTeam(@RequestBody InvitaTeamRequest req) {
        try {
            MembroTeam membroTeam = teamService.findMembroTeamById(req.teamId(), req.mittenteId());
            Utente utente = utenteService.findById(req.utenteId());
            Team team = teamService.findTeamById(req.teamId());

            if (!membroTeam.isAmministratore()) {
                return ResponseEntity.badRequest().body("Solo un amministratore del team può inviare inviti");
            }

            invitiHandler.creaInvitoTeam(membroTeam, utente, team);
            return ResponseEntity.ok("Invito inviato al membro del team");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/rispondi")
    public ResponseEntity<String> rispondi(@RequestBody RispostaInvitoRequest req) {
        try {
            Invito invito = invitoService.findById(req.invitoId());
            invitiHandler.rispostaInvito(invito, req.accetta());
            return ResponseEntity.ok("Risposta registrata");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<?> getInvitiHackathon(@PathVariable String hackathonId) {
        try {
            List<Invito> inviti = invitoService.findAll().stream()
                    .filter(i -> i instanceof InvitoHackathon ih &&
                            ih.getHackathon().getHackathonID().equals(hackathonId))
                    .toList();
            return ResponseEntity.ok(inviti);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<?> getInvitiTeam(@PathVariable String teamId) {
        try {
            List<Invito> inviti = invitoService.findAll().stream()
                    .filter(i -> i instanceof InvitoTeam it &&
                            it.getTeam().getTeamID().equals(teamId))
                    .toList();
            return ResponseEntity.ok(inviti);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}