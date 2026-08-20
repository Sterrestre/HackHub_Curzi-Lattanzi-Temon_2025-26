package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.*;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.InfoHack;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.model.staff.Mentore;
import it.unicam.cs.ids.security.UtenteCorrente;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.UtenteService;
import it.unicam.cs.ids.handler.HackHandler;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.TeamService;
import it.unicam.cs.ids.model.hackathon.InfoHackBuilderImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller per la gestione degli hackathon.
 * Parla con HackathonService e UtenteService per recuperare le entità.
 */
@RestController
@RequestMapping("/api/hackathon")
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
    public ResponseEntity<?> crea(@RequestBody CreaHackathonRequest req,
                                  @UtenteCorrente Utente organizzatore) {
        try {
            InfoHack info = new InfoHackBuilderImpl()
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
    public ResponseEntity<?> conferma(@PathVariable String id, @UtenteCorrente Utente utente) {
        try {
            Hackathon hackathon = hackathonService.getHackathonByID(id);

            if (!hackathon.getOrganizzatore().getUtenteID().equals(utente.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può confermare questo hackathon");
            }

            hackathonService.aggiornaStato(id, Stato.CONFERMATO);
            return ResponseEntity.ok("Hackathon confermato");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/stato")
    public ResponseEntity<?> cambiaStato(@PathVariable String id,
                                         @RequestBody Stato nuovoStato,
                                         @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            if (!hack.getOrganizzatore().getUtenteID().equals(utenteCorrente.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può cambiare lo stato dell'hackathon");
            }
            hackathonService.aggiornaStato(id, nuovoStato);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> elimina(@PathVariable String id,
                                     @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            if (!hack.getOrganizzatore().getUtenteID().equals(utenteCorrente.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può eliminare questo hackathon");
            }
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
                                         @RequestBody IscriviTeamRequest req,
                                         @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            Team team = teamService.findTeamById(req.teamId());
            MembroTeam admin = teamService.findMembroTeamByUtente(req.teamId(), utenteCorrente.getUtenteID());
            TeamIscritto teamIscritto = hackHandler.iscriviTeam(team, hack, admin);
            hackathonService.salva(hack);
            return ResponseEntity.ok(new IscrizioneTeamDTO(
                    teamIscritto.getId(),
                    "Team iscritto con successo"
            ));
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
    public ResponseEntity<String> assegnaMentore(@RequestBody AssegnaMentoreRequest req,
                                                 @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(req.hackathonId());
            if (!hack.getOrganizzatore().getUtenteID().equals(utenteCorrente.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può assegnare un mentore");
            }
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
                                           @RequestBody IscriviMembroRequest req,
                                           @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            MembroTeam membro = teamService.findMembroTeamById(req.teamId(), req.membroId());
            if (!membro.getUtente().getUtenteID().equals(utenteCorrente.getUtenteID())) {
                return ResponseEntity.status(403).body("Puoi iscrivere solo te stesso");
            }
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
    public ResponseEntity<?> confermaClassifica(@PathVariable String id,
                                                @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            if (!hack.getOrganizzatore().getUtenteID().equals(utenteCorrente.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può confermare la classifica");
            }
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
                                          @RequestBody SetVincitoreRequest req,
                                          @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            if (!hack.getOrganizzatore().getUtenteID().equals(utenteCorrente.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può impostare il vincitore");
            }
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
                                                   @RequestBody ApplicaPenalizzazioneRequest req,
                                                   @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            if (!utenteCorrente.puoPenalizzare(hack)) {
                return ResponseEntity.status(403).body("Non hai i permessi per applicare una penalizzazione");
            }
            if (!hackHandler.validaDati(hack, req.teamId(), req.tipoIntervento(), req.motivazione())) {
                return ResponseEntity.badRequest().body("Dati non validi per la penalizzazione");
            }
            hackHandler.applicaPenalizzazione(hack, req.teamId(), req.tipoIntervento(), req.motivazione());
            hackathonService.salva(hack);
            return ResponseEntity.ok("Penalizzazione applicata");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Errore interno: " + e.getMessage());
        }
    }

    @PostMapping("/{id}/valida-presenze")
    public ResponseEntity<?> validaPresenze(@PathVariable String id,
                                            @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            if (!hack.getOrganizzatore().getUtenteID().equals(utenteCorrente.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può validare le presenze");
            }
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
                                             @RequestBody ImpostaPresenzaRequest req,
                                             @UtenteCorrente Utente utenteCorrente) {
        try {
            Hackathon hack = hackathonService.getHackathonByID(id);
            if (!hack.getOrganizzatore().getUtenteID().equals(utenteCorrente.getUtenteID())) {
                return ResponseEntity.status(403).body("Solo l'organizzatore può impostare le presenze");
            }
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

    @GetMapping("/{id}/sono-staff")
    public ResponseEntity<Boolean> sonoStaff(@PathVariable String id,
                                             @UtenteCorrente Utente utenteCorrente) {
        Hackathon hack = hackathonService.getHackathonByID(id);
        return ResponseEntity.ok(utenteCorrente.isStaffPerHackathon(hack));
    }

    @GetMapping("/{id}/staff")
    public ResponseEntity<StaffHackathonDTO> getStaff(@PathVariable String id) {
        Hackathon hack = hackathonService.getHackathonByID(id);
        return ResponseEntity.ok(StaffHackathonDTO.from(hack));
    }
}