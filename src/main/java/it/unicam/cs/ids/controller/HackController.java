package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.*;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.InfoHack;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.model.staff.Mentore;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.UtenteService;
import it.unicam.cs.ids.handler.HackHandler;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione degli hackathon.
 * Parla con HackathonService e UtenteService per recuperare le entità.
 */
@RestController
@RequestMapping("/hackathon")
@Transactional
public class HackController {

    private final HackathonService hackathonService;
    private final UtenteService utenteService;
    private final HackHandler hackHandler;
    private final TeamService teamService;

    public HackController(HackathonService hackathonService, UtenteService utenteService, HackHandler hackHandler, TeamService teamService) {
        this.hackathonService = hackathonService;
        this.utenteService = utenteService;
        this.hackHandler = hackHandler;
        this.teamService = teamService;
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

    @PostMapping("/{id}/iscrivi-team")
    public ResponseEntity<?> iscriviTeam(@PathVariable String id,
                                         @RequestBody IscriviTeamRequest req) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            Team team = teamService.findTeamById(req.teamId());
            MembroTeam admin = teamService.findMembroTeamById(req.teamId(), req.amministratoreId());
            TeamIscritto teamIscritto = hackHandler.iscriviTeam(team, hack, admin);
            hackathonService.salva(hack);
            return ResponseEntity.ok("Team iscritto con successo. ID iscrizione: " + teamIscritto.getId());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/classifica")
    public ResponseEntity<?> getClassifica(@PathVariable String id) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            List<TeamIscritto> classifica = hackHandler.calcolaClassificaPreliminare(hack);
            hackathonService.salva(hack);
            List<String> risultato = classifica.stream()
                    .map(t -> t.getTeam().getNome() + " - voto: " +
                            (t.getSottomissione() != null && t.getSottomissione().getValutazione() != null
                                    ? t.getSottomissione().getValutazione().getVoto()
                                    : "non valutato"))
                    .toList();
            return ResponseEntity.ok(risultato);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/assegna-mentore")
    public ResponseEntity<String> assegnaMentore(@RequestBody AssegnaMentoreRequest req) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(req.hackathonId());
            TeamIscritto team = hack.getTeamIscrittoById(req.teamId());
            Mentore mentore = (Mentore) hack.getRuoli().stream()
                    .filter(r -> r.getId().equals(req.ruoloMentoreId()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("Mentore non trovato"));
            hackHandler.assegnaMentore(team, mentore);
            hackathonService.salva(hack);
            return ResponseEntity.ok("Mentore assegnato al team");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/iscrivi-membro")
    public ResponseEntity<?> iscriviMembro(@PathVariable String id,
                                           @RequestBody IscriviMembroRequest req) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            MembroTeam membro = teamService.findMembroTeamById(req.teamId(), req.membroId());
            String risultato = hackHandler.iscriviMembroteam(membro, hack);
            hackathonService.salva(hack);
            return ResponseEntity.ok(risultato);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/conferma-classifica")
    public ResponseEntity<?> confermaClassifica(@PathVariable String id) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            hackHandler.confermaClassifica(hack);
            hackathonService.salva(hack);
            return ResponseEntity.ok("Classifica confermata");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/vincitore")
    public ResponseEntity<?> setVincitore(@PathVariable String id,
                                          @RequestBody SetVincitoreRequest req) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            TeamIscritto team = hack.getTeamIscrittoById(req.teamId());
            hackHandler.setTeamVincitore(hack, team);
            hackathonService.salva(hack);
            return ResponseEntity.ok("Team vincitore impostato: " + team.getTeam().getNome());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/penalizzazione")
    public ResponseEntity<?> applicaPenalizzazione(@PathVariable String id,
                                                   @RequestBody ApplicaPenalizzazioneRequest req) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            if (!hackHandler.validaDati(hack, req.teamId(), req.tipoIntervento(), req.motivazione())) {
                return ResponseEntity.badRequest().body("Dati non validi per la penalizzazione");
            }
            hackHandler.applicaPenalizzazione(hack, req.teamId(), req.tipoIntervento(), req.motivazione());            hackathonService.salva(hack);
            return ResponseEntity.ok("Penalizzazione applicata");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/valida-presenze")
    public ResponseEntity<?> validaPresenze(@PathVariable String id) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            hackHandler.validaPresenze(hack);
            return ResponseEntity.ok("Presenze validate");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/imposta-presenza")
    public ResponseEntity<?> impostaPresenza(@PathVariable String id,
                                             @RequestBody ImpostaPresenzaRequest req) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            TeamIscritto team = hack.getTeamIscrittoById(req.teamId());
            team.getElencoIscritti().stream()
                    .filter(m -> m.getUtente().getUtenteID().equals(req.utenteId()))
                    .findFirst()
                    .ifPresent(m -> hackHandler.impostaPresenza(m, req.presente()));
            hackathonService.salva(hack);
            return ResponseEntity.ok("Presenza impostata");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }
}