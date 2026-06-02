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

@RestController
@RequestMapping("/supporto")
public class SupportoController {

    // TODO crea RichiestaSupportoService e controlla questo controller

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

        RichiestaSupporto richiesta = richiestaSupportoService.findById(req.richiestaId());
        supportoHandler.rispondiAllaRichiesta(richiesta);

        return ResponseEntity.ok("Richiesta di supporto risolta");
    }

    @PostMapping("/proponi-call")
    public ResponseEntity<String> proponiCall(@RequestBody ProponiCallRequest req) {

        RichiestaSupporto richiesta = richiestaSupportoService.findById(req.richiestaId());
        Hackathon hack = hackathonService.getHackathonByID(richiesta.getHackathonId());

        supportoHandler.richiestaProponiCall(
                req.dataOra(),
                richiesta,
                hack.getInfoHack().getDataFine()
        );

        return ResponseEntity.ok("Proposta di call inviata");
    }

    @PostMapping("/annulla-call")
    public ResponseEntity<String> annullaCall() {
        supportoHandler.annullaCall();
        return ResponseEntity.ok("Call annullata");
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
                .entrySet()
                .stream()
                .map(RichiestaSupportoDTO::from)
                .toList();

        return ResponseEntity.ok(lista);
    }
}

