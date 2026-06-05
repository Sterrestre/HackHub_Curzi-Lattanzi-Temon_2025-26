package it.unicam.cs.ids.service;

import it.unicam.cs.ids.handler.HackHandler;
import it.unicam.cs.ids.handler.InvitiHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.*;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.repository.HackathonRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Service per la gestione degli hackathon.
 * Coordina il recupero delle entità dal repository, delega la logica
 * di dominio all'HackHandler e si occupa della persistenza.
 */
@Service
public class HackathonService {

    private final HackathonRepository hackathonRepository;
    private final InvitiHandler invitiHandler;
    private final HackHandler hackHandler;

    public HackathonService(HackathonRepository hackathonRepository, InvitiHandler invitiHandler, HackHandler hackHandler) {
        this.hackathonRepository = hackathonRepository;
        this.invitiHandler = invitiHandler;
        this.hackHandler = hackHandler;
    }

    /**
     * Crea un nuovo hackathon, delega la logica all'handler e salva.
     */
    public Hackathon creaHackathon(Utente organizzatore, String nome, InfoHack info) {
        Hackathon h = hackHandler.creaHackathon(organizzatore, nome, info);
        h.cambiaStato(new BozzaState(invitiHandler));
        hackathonRepository.save(h);
        return h;
    }

    /**
     * Recupera un hackathon per ID e ricostruisce il suo stato.
     */
    public Hackathon getHackathonByID(String id) {
        Hackathon h = hackathonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Hackathon non trovato"));
        ricostruisciState(h);
        return h;
    }

    /**
     * Restituisce tutti gli hackathon registrati.
     */
    public Collection<Hackathon> getTutti() {
        return hackathonRepository.findAll();
    }

    /**
     * Aggiorna lo stato di un hackathon.
     */
    public void aggiornaStato(String id, Stato nuovoStato) {
        Hackathon h = getHackathonByID(id);
        h.stato = nuovoStato;
        ricostruisciState(h);
        hackathonRepository.save(h);
    }

    /**
     * Aggiunge un team iscritto a un hackathon.
     */
    public void aggiungiTeamIscritto(String id, TeamIscritto team) {
        Hackathon h = getHackathonByID(id);
        h.getTeamIscritti().add(team);
        hackathonRepository.save(h);
    }

    /**
     * Rimuove un hackathon dal sistema.
     */
    public void eliminaHackathon(String id) {hackathonRepository.deleteById(id);
    }

    // Helper per ricostruire lo State partendo dallo stato salvato nel Repository
    private void ricostruisciState(Hackathon h) {
        Stato stato = h.getStato();

        HackState state = switch (stato) {
            case BOZZA -> new BozzaState(invitiHandler);
            case CONFERMATO -> new ConfermatoState(h);
            case IN_CORSO -> new InCorsoState();
            case CONCLUSO -> new ConclusoState();
        };

        h.cambiaStato(state);
    }

    public Hackathon salva(Hackathon hackathon) {
        return hackathonRepository.save(hackathon);
    }
}

