package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.InfoHack;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.model.team.TeamIscritto;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
public class HackathonService {

    private final Map<String, Hackathon> hackathonMap = new HashMap<>();

    /**
     * Crea un nuovo hackathon e lo registra nel sistema.
     */
    public Hackathon creaHackathon(InfoHack info, String nome) {
        Hackathon h = new Hackathon(info, nome);
        hackathonMap.put(h.getHackathonID(), h);
        return h;
    }

    /**
     * Recupera un hackathon tramite il suo ID.
     */
    public Hackathon getHackathon(String id) {
        Hackathon h = hackathonMap.get(id);
        if (h == null) {
            throw new IllegalArgumentException("Hackathon non trovato: " + id);
        }
        return h;
    }

    /**
     * Restituisce tutti gli hackathon registrati.
     */
    public Collection<Hackathon> getTutti() {
        return hackathonMap.values();
    }

    /**
     * Aggiorna lo stato di un hackathon.
     */
    public void aggiornaStato(String id, Stato nuovoStato) {
        Hackathon h = getHackathon(id);
        h.stato = nuovoStato;
    }

    /**
     * Aggiunge un team iscritto a un hackathon.
     */
    public void aggiungiTeam(String id, TeamIscritto team) {
        Hackathon h = getHackathon(id);
        h.getTeamIscritti().add(team);
    }

    /**
     * Rimuove un hackathon dal sistema.
     */
    public void eliminaHackathon(String id) {
        hackathonMap.remove(id);
    }
}

