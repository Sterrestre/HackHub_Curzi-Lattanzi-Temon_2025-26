package it.unicam.cs.ids.service;

import it.unicam.cs.ids.handler.SottomissioneHandler;
import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
import org.springframework.stereotype.Service;

/**
 * Service applicativo per la gestione delle sottomissioni.
 *
 * Responsabilità di questo layer:
 *  - Recuperare le entità necessarie tramite i repository
 *  - Delegare la logica di dominio a SottomissioneHandler
 *  - Salvare le modifiche tramite HackathonRepository
 *
 * NON contiene logica di dominio: quella rimane nell'handler e nel modello.
 */
@Service
public class SottomissioneService {

    private final SottomissioneHandler sottomissioneHandler;
    private final HackathonService hackathonService;

    public SottomissioneService(SottomissioneHandler sottomissioneHandler,
                                HackathonService hackathonService) {
        this.sottomissioneHandler = sottomissioneHandler;
        this.hackathonService = hackathonService;
    }

    /**
     * Carica la sottomissione di un team iscritto recuperando le entità per ID.
     * Verifica che il team appartenga all'hackathon specificato prima di procedere.
     *
     * @param hackathonId    ID dell'hackathon
     * @param teamIscrittoId ID del team iscritto
     * @param titolo         titolo della sottomissione
     * @param descrizione    descrizione della sottomissione
     * @param linkRepository link al repository del progetto
     */
    public Sottomissione caricaSottomissione(String hackathonId, String teamIscrittoId,
                                             String titolo, String descrizione, String linkRepository) {
        Hackathon hackathon = hackathonService.getHackathonByID(hackathonId);
        TeamIscritto teamIscritto = hackathon.getTeamIscrittoById(teamIscrittoId);

        sottomissioneHandler.caricaSottomissione(teamIscritto, titolo, descrizione, linkRepository);
        hackathonService.salva(hackathon);
        return teamIscritto.getSottomissione();
    }

    /**
     * Valuta la sottomissione di un team iscritto recuperando le entità per ID.
     *
     * @param giudiceId      ID del giudice che effettua la valutazione
     * @param hackathonId    ID dell'hackathon
     * @param teamIscrittoId ID del team iscritto
     * @param voto           voto numerico [0, 10]
     * @param giudizio       testo del giudizio
     */
    public Sottomissione valutaSottomissione(String giudiceId, String hackathonId,
                                             String teamIscrittoId, double voto, String giudizio) {
        Hackathon hackathon = hackathonService.getHackathonByID(hackathonId);
        TeamIscritto teamIscritto = hackathon.getTeamIscrittoById(teamIscrittoId);
        Utente giudice = hackathon.getRuoli().stream()
                .filter(r -> r.getUtente().getUtenteID().equals(giudiceId))
                .map(r -> r.getUtente())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Giudice non trovato nell'hackathon"));

        sottomissioneHandler.valutaSottomissione(giudice, hackathon, teamIscritto, voto, giudizio);
        hackathonService.salva(hackathon);
        return teamIscritto.getSottomissione();
    }

    /**
     * Restituisce la sottomissione di un team iscritto recuperando le entità per ID.
     *
     * @param hackathonId    ID dell'hackathon
     * @param teamIscrittoId ID del team iscritto
     * @return la sottomissione associata, o null se non ancora caricata
     */
    public Sottomissione visualizzaSottomissione(String hackathonId, String teamIscrittoId) {
        Hackathon hackathon = hackathonService.getHackathonByID(hackathonId);
        TeamIscritto teamIscritto = hackathon.getTeamIscrittoById(teamIscrittoId);
        return sottomissioneHandler.visualizzaSottomissione(teamIscritto);
    }

    /**
     * Restituisce l'hackathon associato a un team iscritto.
     *
     * @param teamIscritto il team di cui recuperare l'hackathon
     * @return l'hackathon associato
     * @throws IllegalArgumentException se teamIscritto è null
     */
    public Hackathon getHackathon(TeamIscritto teamIscritto) {
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto non può essere null.");
        }
        return sottomissioneHandler.getHackathon(teamIscritto);
    }
}