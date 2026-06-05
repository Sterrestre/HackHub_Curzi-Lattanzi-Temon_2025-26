package it.unicam.cs.ids.service;

import it.unicam.cs.ids.handler.SottomissioneHandler;
import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.StatoSottomissione;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.Valutazione;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.repository.HackathonRepository;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * Service applicativo per la gestione delle sottomissioni.
 *
 * Responsabilità di questo layer:
 *  - Recuperare le entità necessarie tramite i rispettivi service
 *  - Verificare i permessi dell'utente sull'hackathon (es. puoValutare)
 *  - Validare i dati di input (voto nel range, giudizio non vuoto)
 *  - Costruire l'oggetto Valutazione
 *  - Delegare all'SottomissioneHandler per l'operazione di dominio
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
     * Carica o sostituisce la sottomissione di un team iscritto.
     * Verifica che il team appartenga all'hackathon specificato prima di procedere.
     *
     * @param hackathonId     ID dell'hackathon
     * @param sottomissioneFile file da caricare
     * @throws IllegalArgumentException se i parametri sono null o il team non appartiene all'hackathon
     */
    public void caricaSottomissione(String hackathonId, String teamIscrittoId, File sottomissioneFile) {
        if (sottomissioneFile == null) {
            throw new IllegalArgumentException("File sottomissione non può essere null.");
        }

        Hackathon hackathon = hackathonService.getHackathonByID(hackathonId);
        TeamIscritto teamIscritto = hackathon.getTeamIscrittoById(teamIscrittoId);

        sottomissioneHandler.caricaSottomissione(teamIscritto, sottomissioneFile);
        hackathonService.salva(hackathon);
    }

    /**
     * Valuta la sottomissione di un team iscritto.
     *
     * Controlla che:
     *  1. L'utente abbia il ruolo di giudice per quell'hackathon (puoValutare)
     *  2. Il voto sia nel range valido [0, 10]
     *  3. Il giudizio non sia null o vuoto
     *  4. La sottomissione sia nello stato CARICATA (non MANCANTE, non già VALUTATA)
     *
     * @param utente       l'utente che effettua la valutazione (deve essere un giudice)
     * @param hackathonId  ID dell'hackathon
     * @param teamIscritto il team la cui sottomissione va valutata
     * @param voto         voto numerico [0, 10] (viene arrotondato a intero)
     * @param giudizio     testo del giudizio
     * @throws SecurityException        se l'utente non ha il permesso di valutare
     * @throws IllegalArgumentException se voto o giudizio non sono validi
     * @throws IllegalStateException    se la sottomissione non è nello stato corretto
     */
    public void valutaSottomissione(Utente utente, String hackathonId,
                                    TeamIscritto teamIscritto, double voto, String giudizio) {
        Hackathon hackathon = hackathonService.getHackathonByID(hackathonId);
        sottomissioneHandler.valutaSottomissione(utente, hackathon, teamIscritto, voto, giudizio);
        hackathonService.salva(hackathon);
    }

    /**
     * Restituisce la sottomissione di un team iscritto.
     *
     * @param teamIscritto il team di cui recuperare la sottomissione
     * @return la sottomissione associata, o null se non ancora caricata
     * @throws IllegalArgumentException se teamIscritto è null
     */
    public Sottomissione visualizzaSottomissione(TeamIscritto teamIscritto) {
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto non può essere null.");
        }
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