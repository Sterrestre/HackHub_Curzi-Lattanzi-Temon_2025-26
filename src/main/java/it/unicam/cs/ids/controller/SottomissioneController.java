package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.dto.CreaSottomissioneRequest;
import it.unicam.cs.ids.dto.SottomissioneDTO;
import it.unicam.cs.ids.dto.ValutaSottomissioneRequest;
import it.unicam.cs.ids.handler.SottomissioneHandler;
import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sottomissioni")
public class SottomissioneController {

    // TODO revisionare il controller.
    // Non esiste sottomissioneId (niente repository, si recuepera da hackathon --> teamIscritto) <-- MODIFICARE IN MODO COERENTE QUESTA CLASSE
    // Fai parlare il controller SOLO con i Service --> i service chiamano gli Handler che contengono la logica di dominio.
    // Ricordarsi di salvare l'entity alla fine dei metodi del service che lo richiedono
    // Fare i try catch per ogni PostMapping

    private final SottomissioneHandler sottomissioneHandler;
    private final TeamService teamService;
    private final HackathonService hackService;

    public SottomissioneController(SottomissioneHandler sottomissioneHandler,
                                   TeamService teamService,
                                   HackathonService hackService) {
        this.sottomissioneHandler = sottomissioneHandler;
        this.teamService = teamService;
        this.hackService = hackService;
    }

    @PostMapping("/crea")
    public ResponseEntity<SottomissioneDTO> crea(@RequestBody CreaSottomissioneRequest req) {

        // TODO nota Matteo
        // le fa SpringBoot volendo <-- ???
        // TODO controlla che recuperi anche i TeamIscritti
        TeamIscritto team = teamService.findTeamIscrittoById(req.teamIscrittoId());
        Hackathon hack = hackService.getHackathonByID(req.hackathonId());

        // TODO
        Sottomissione s = sottomissioneHandler.creaSottomissione(team, hack, req.titolo(), req.descrizione());

        // TODO nota Matteo
        // Non correttissimo, perché per forza ti dà l'ok. Se sotto va in errore, responseEntity deve dare errore
        return ResponseEntity.ok(SottomissioneDTO.from(s));
    }

    @PostMapping("/valuta")
    public ResponseEntity<SottomissioneDTO> valuta(@RequestBody ValutaSottomissioneRequest req) {

        Sottomissione s = sottomissioneHandler.valutaSottomissione(req.sottomissioneId(), req.voto(), req.giudizio());

        return ResponseEntity.ok(SottomissioneDTO.from(s));
    }

    @GetMapping("/{sottomissioneId}")
    public ResponseEntity<SottomissioneDTO> getById(@PathVariable String sottomissioneId) {

        Sottomissione s = sottomissioneHandler.getSottomissione(sottomissioneId);
        return ResponseEntity.ok(SottomissioneDTO.from(s));
    }

    // TODO nota Matteo
    // MACELLO DA GESTIRE IL DOPPIO CAMPO NEL LINK
    @GetMapping("/hackathon/{hackathonId}")
    public ResponseEntity<List<SottomissioneDTO>> getByHackathon(@PathVariable String hackathonId) {

        Hackathon hack = hackService.getHackathonByID(hackathonId);

        List<SottomissioneDTO> lista = hack.getSottomissioni().stream()
                .map(SottomissioneDTO::from)
                .toList();

        return ResponseEntity.ok(lista);
    }
}

