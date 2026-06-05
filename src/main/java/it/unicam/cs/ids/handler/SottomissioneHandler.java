package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.StatoSottomissione;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.model.Valutazione;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Handler per la gestione delle sottomissioni dei team agli hackathon.
 *
 * Responsabilità di questo layer:
 *  - Operazioni di dominio sulle sottomissioni (carica, valuta, recupera)
 *  - NON gestisce permessi né validazioni applicative: quelle competono a SottomissioneService
 */
@Service
public class SottomissioneHandler {

    /**
     * Carica o sostituisce la sottomissione di un team iscritto.
     * Se la sottomissione non esiste, ne crea una nuova e la associa al team.
     * Se esiste già, la sostituisce con il nuovo file.
     *
     * @param teamIscritto     il team iscritto al quale associare la sottomissione
     * @param sottomissioneFile il file della sottomissione da caricare
     * @throws IllegalArgumentException se teamIscritto o sottomissioneFile sono nulli
     */
    public void caricaSottomissione(TeamIscritto teamIscritto, File sottomissioneFile) {
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto nullo");
        }
        if (sottomissioneFile == null) {
            throw new IllegalArgumentException("File sottomissione nullo");
        }

        Sottomissione sottomissione = teamIscritto.getSottomissione();
        if (sottomissione == null) {
            sottomissione = new Sottomissione(teamIscritto);
            teamIscritto.setSottomissione(sottomissione);
        }

        sottomissione.setFile(sottomissioneFile);
        sottomissione.setDataCaricamento(LocalDateTime.now());
        sottomissione.setStatoSottomissione(StatoSottomissione.CARICATA);
    }

    // TODO commento non coerente con il metodo
    /**
     * Valuta una sottomissione di un team iscritto.
     * La sottomissione deve essere stata precedentemente caricata e non deve essere nello stato MANCANTE.
     * La verifica dei permessi e la costruzione della Valutazione competono a SottomissioneService.
     *
     * @param teamIscritto il team iscritto cui appartiene la sottomissione
     * @throws IllegalArgumentException se teamIscritto o valutazione sono nulli
     * @throws IllegalStateException    se la sottomissione non è stata caricata o è nello stato MANCANTE
     */
    public void valutaSottomissione(Utente utente, Hackathon hackathon, TeamIscritto teamIscritto, double voto, String giudizio) {
        {
            if (utente == null) throw new IllegalArgumentException("Utente nullo");
            if (hackathon == null) throw new IllegalArgumentException("Hackathon nullo");
            if (teamIscritto == null) throw new IllegalArgumentException("TeamIscritto nullo");

            // Controllo del permesso
            if (!utente.puoValutare(hackathon)) {
                throw new SecurityException("L'utente non può valutare in questo hackathon");
            }

            // Validazione voto e giudizio
            if (voto < 0 || voto > 10) {
                throw new IllegalArgumentException("Voto fuori range");
            }
            if (giudizio == null || giudizio.isBlank()) {
                throw new IllegalArgumentException("Giudizio vuoto");
            }


            Sottomissione sottomissione = teamIscritto.getSottomissione();
            if (sottomissione == null || sottomissione.getStatoSottomissione() == StatoSottomissione.MANCANTE) {
                throw new IllegalStateException("Impossibile valutare: sottomissione mancante");
            }

            if (sottomissione.getStatoSottomissione() == StatoSottomissione.VALUTATA) {
                throw new IllegalStateException("La sottomissione è già stata valutata");
            }

            // Creazione della valutazione e aggiornamento della valutazione
            Valutazione valutazione = new Valutazione((int) Math.round(voto), giudizio);

            sottomissione.setValutazione(valutazione);
            sottomissione.setStatoSottomissione(StatoSottomissione.VALUTATA);
        }
    }

    /**
     * Visualizza la sottomissione di un team iscritto.
     *
     * @param teamIscritto il team iscritto di cui recuperare la sottomissione
     * @return la sottomissione associata al team, o null se non è stata caricata alcuna sottomissione
     * @throws IllegalArgumentException se teamIscritto è nullo
     */
    public Sottomissione visualizzaSottomissione(TeamIscritto teamIscritto) {
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto nullo");
        }

        return teamIscritto.getSottomissione();
    }

    /**
     * Recupera l'hackathon associato a un team iscritto.
     *
     * @param teamIscritto il team iscritto di cui recuperare l'hackathon
     * @return l'hackathon associato al team
     * @throws IllegalArgumentException se teamIscritto è nullo
     */
    public Hackathon getHackathon(TeamIscritto teamIscritto) {
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto nullo");
        }

        return teamIscritto.getHackathon();
    }

    /**
     * Crea una nuova sottomissione per un team in un hackathon.
     *
     * @param team        il team per il quale creare la sottomissione
     * @return la sottomissione creata nello stato MANCANTE
     */
    public Sottomissione creaSottomissione(TeamIscritto team) {
        if (team == null) {
            throw new IllegalArgumentException("Team nullo");
        }

        Sottomissione sottomissione = new Sottomissione(team);
        return sottomissione;
    }

    /**
     * Recupera una sottomissione per ID.
     *
     * @param sottomissioneId l'ID della sottomissione da recuperare
     * @return la Sottomissione con l'ID fornito
     * @throws IllegalArgumentException se sottomissioneId è nullo o vuoto o se non esiste
     */
    public Sottomissione getSottomissione(String sottomissioneId) {
        if (sottomissioneId == null || sottomissioneId.isBlank()) {
            throw new IllegalArgumentException("Id sottomissione nullo");
        }

        // TODO

        return sottomissioneRepository.findByID(sottomissioneId);
    }
}