package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.CambiaRuoloRequest;
import it.unicam.cs.ids.dto.LasciaTeamRequest;
import it.unicam.cs.ids.dto.MembroTeamDTO;
import it.unicam.cs.ids.handler.MembriTeamHandler;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.service.UtenteService;
import it.unicam.cs.ids.service.team.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/membri")
public class MembriTeamController {

    // TODO Hai già creato UtenteService? Rivedi anche questo controller

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

//        MembroTeam membro = membriTeamHandler.findById(request.membroId());
//        membriTeamHandler.rimuoviMembroTeam(membro);;

        return ResponseEntity.ok("Il membro ha lasciato il team");
    }

    @PostMapping("/cambia-ruolo")
    public ResponseEntity<MembroTeamDTO> cambiaRuolo(@RequestBody CambiaRuoloRequest request) {

//        MembroTeam membro = membriTeamHandler.findById(request.membroId());
//        MembroTeam aggiornato = membriTeamHandler.rendiAmministratore(membro, request.nuovoStatoAmministratore());

//        return ResponseEntity.ok(MembroTeamDTO.from(aggiornato));
        // ELIMINA QUESTO SOTTO QUANDO HAI RISOLTO QUELLO SOPRA:
        return ResponseEntity.ok().build();
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<List<MembroTeamDTO>> getMembriTeam(@PathVariable String teamId) {

        Team team = teamService.findById(teamId);

        List<MembroTeamDTO> membri = team.getMembri().stream()
                .map(MembroTeamDTO::from)
                .toList();

        return ResponseEntity.ok(membri);
    }

    @GetMapping("/{membroId}")
    public ResponseEntity<MembroTeamDTO> getMembro(@PathVariable Long membroId) {

//        MembroTeam membro = membriTeamHandler.findById(membroId);

 //       return ResponseEntity.ok(MembroTeamDTO.from(membro));
        // ELIMINA QUESTO SOTTO QUANDO HAI RISOLTO QUELLO SOPRA:
        return ResponseEntity.ok().build();
    }
}

