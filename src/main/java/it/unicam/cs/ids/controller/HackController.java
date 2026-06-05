package it.unicam.cs.ids.controller;
import it.unicam.cs.ids.dto.CreaHackathonRequest;
import it.unicam.cs.ids.dto.HackathonDTO;
import it.unicam.cs.ids.handler.HackHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.InfoHack;
import it.unicam.cs.ids.model.hackathon.Stato;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hackathon")
public class HackController {

    // TODO rivedi questa classe e aggiungi i metodi mancanti in HackHandler.

    private final HackHandler hackHandler;

    public HackController(HackHandler hackHandler) {
        this.hackHandler = hackHandler;
    }

    @PostMapping("/crea")
    public ResponseEntity<HackathonDTO> crea(@RequestBody CreaHackathonRequest req) {

    //    Utente organizzatore = utenteService.findById(req.organizzatoreId());ù
        // ELIMINA QUESTO SOTTO QUANDO HAI RISOLTO QUELLO SOPRA:
        Utente organizzatore = new Utente("fiewo", "huo", "huo", "hui", "nono", "hui", LocalDate.now());

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


        Hackathon h = hackHandler.creaHackathon(
                organizzatore,
                req.nome(),
                info
        );

        return ResponseEntity.ok(HackathonDTO.from(h));
    }


    @GetMapping("/{id}")
    public ResponseEntity<HackathonDTO> getHackathon(@PathVariable String id) {

        Hackathon h = hackHandler.getHackathonById(id);
        return ResponseEntity.ok(HackathonDTO.from(h));
    }

    @GetMapping("/all")
    public ResponseEntity<List<HackathonDTO>> getAll() {

        List<HackathonDTO> lista = hackHandler.getAllHackathon()
                .stream()
                .map(HackathonDTO::from)
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping("/{id}/conferma")
    public ResponseEntity<String> conferma(@PathVariable String id) {

        hackHandler.confermaHackathon(id);
        return ResponseEntity.ok("Hackathon confermato");
    }

    @PostMapping("/{id}/stato")
    public ResponseEntity<?> cambiaStato(@PathVariable String id, @RequestBody Stato nuovoStato) {
        hackHandler.cambiaStato(id, nuovoStato);
        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> elimina(@PathVariable String id) {

        hackHandler.eliminaHackathon(id);
        return ResponseEntity.ok("Hackathon eliminato");
    }
}

