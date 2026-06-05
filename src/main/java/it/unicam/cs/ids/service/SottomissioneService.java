package it.unicam.cs.ids.service;

import it.unicam.cs.ids.handler.SottomissioneHandler;
import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.StatoSottomissione;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.Valutazione;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
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
     * @param teamIscritto    team che carica la sottomissione
     * @param sottomissioneFile file da caricare
     * @throws IllegalArgumentException se i parametri sono null o il team non appartiene all'hackathon
     */
    public void caricaSottomissione(String hackathonId, TeamIscritto teamIscritto, File sottomissioneFile) {
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalArgumentException("ID hackathon non può essere null o vuoto.");
        }
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto non può essere null.");
        }
        if (sottomissioneFile == null) {
            throw new IllegalArgumentException("File sottomissione non può essere null.");
        }

        Hackathon hackathon = hackathonService.getHackathonByID(hackathonId);

        boolean teamAppartiene = hackathon.getTeamIscritti().contains(teamIscritto);
        if (!teamAppartiene) {
            throw new IllegalStateException("Il team non è iscritto a questo hackathon.");
        }

        sottomissioneHandler.caricaSottomissione(teamIscritto, sottomissioneFile);
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
        if (utente == null) {
            throw new IllegalArgumentException("Utente non può essere null.");
        }
        if (hackathonId == null || hackathonId.isBlank()) {
            throw new IllegalArgumentException("ID hackathon non può essere null o vuoto.");
        }
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto non può essere null.");
        }

        //  Recupera l'hackathon e verifica il permesso
        Hackathon hackathon = hackathonService.getHackathonByID(hackathonId);

        if (!utente.puoValutare(hackathon)) {
            throw new SecurityException(
                    "L'utente " + utente.getNickname() + " non ha il permesso di valutare in questo hackathon."
            );
        }

        // Valida il voto
        if (voto < 0 || voto > 10) {
            throw new IllegalArgumentException("Il voto deve essere compreso tra 0 e 10, ricevuto: " + voto);
        }

        // Valida il giudizio
        if (giudizio == null || giudizio.isBlank()) {
            throw new IllegalArgumentException("Il giudizio non può essere null o vuoto.");
        }

        //  Controlla che la sottomissione sia nello stato CARICATA
        Sottomissione sottomissione = teamIscritto.getSottomissione();
        if (sottomissione == null || sottomissione.getStatoSottomissione() == StatoSottomissione.MANCANTE) {
            throw new IllegalStateException("Impossibile valutare: la sottomissione non è stata caricata.");
        }
        if (sottomissione.getStatoSottomissione() == StatoSottomissione.VALUTATA) {
            throw new IllegalStateException("La sottomissione è già stata valutata.");
        }

        // Costruisce la Valutazione e delega all'handler
        Valutazione valutazione = new Valutazione((int) Math.round(voto), giudizio);
        sottomissioneHandler.valutaSottomissione(teamIscritto, valutazione);
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