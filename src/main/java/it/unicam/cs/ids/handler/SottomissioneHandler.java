package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.StatoSottomissione;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.model.Valutazione;
import org.springframework.stereotype.Service;

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
     * Se esiste già, la aggiorna con i nuovi dati.
     *
     * @param teamIscritto  il team iscritto al quale associare la sottomissione
     * @param titolo        titolo della sottomissione
     * @param descrizione   descrizione della sottomissione
     * @param linkRepository link al repository del progetto
     * @throws IllegalArgumentException se teamIscritto o titolo sono nulli
     */
    public void caricaSottomissione(TeamIscritto teamIscritto, String titolo,
                                    String descrizione, String linkRepository) {
        if (teamIscritto == null) throw new IllegalArgumentException("TeamIscritto nullo");
        if (titolo == null || titolo.isBlank()) throw new IllegalArgumentException("Titolo obbligatorio");

        // Una sottomissione puo' essere caricata solo mentre l'hackathon e' in corso:
        // non prima (non e' ancora iniziato) e non dopo (e' gia' concluso)
        if (teamIscritto.getHackathon().getStato() != Stato.IN_CORSO) {
            throw new IllegalStateException("Impossibile caricare la sottomissione: l'hackathon non è in corso");
        }

        Sottomissione sottomissione = teamIscritto.getSottomissione();
        if (sottomissione == null) {
            sottomissione = new Sottomissione(teamIscritto);
            teamIscritto.setSottomissione(sottomissione);
        }

        sottomissione.setTitolo(titolo);
        sottomissione.setDescrizione(descrizione);
        sottomissione.setLinkRepository(linkRepository);
        sottomissione.setDataCaricamento(LocalDateTime.now());
        sottomissione.setStatoSottomissione(StatoSottomissione.CARICATA);
    }

    /**
     * Valuta una sottomissione di un team iscritto.
     * La sottomissione deve essere stata precedentemente caricata e non deve essere nello stato MANCANTE.
     *
     * @param utente       l'utente che effettua la valutazione (deve essere giudice)
     * @param hackathon    l'hackathon in cui avviene la valutazione
     * @param teamIscritto il team iscritto cui appartiene la sottomissione
     * @param voto         voto numerico [0, 10]
     * @param giudizio     testo del giudizio
     * @throws SecurityException        se l'utente non può valutare in questo hackathon
     * @throws IllegalArgumentException se voto o giudizio non sono validi
     * @throws IllegalStateException    se la sottomissione non è nello stato corretto
     */
    public void valutaSottomissione(Utente utente, Hackathon hackathon,
                                    TeamIscritto teamIscritto, double voto, String giudizio) {
        if (utente == null) throw new IllegalArgumentException("Utente nullo");
        if (hackathon == null) throw new IllegalArgumentException("Hackathon nullo");
        if (teamIscritto == null) throw new IllegalArgumentException("TeamIscritto nullo");

        if (!utente.puoValutare(hackathon)) {
            throw new SecurityException("L'utente non può valutare in questo hackathon");
        }
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

        Valutazione valutazione = new Valutazione((int) Math.round(voto), giudizio);
        sottomissione.setValutazione(valutazione);
        sottomissione.setStatoSottomissione(StatoSottomissione.VALUTATA);
    }

    /**
     * Visualizza la sottomissione di un team iscritto.
     *
     * @param teamIscritto il team iscritto di cui recuperare la sottomissione
     * @return la sottomissione associata al team, o null se non è stata caricata alcuna sottomissione
     * @throws IllegalArgumentException se teamIscritto è nullo
     */
    public Sottomissione visualizzaSottomissione(TeamIscritto teamIscritto) {
        if (teamIscritto == null) throw new IllegalArgumentException("TeamIscritto nullo");
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
        if (teamIscritto == null) throw new IllegalArgumentException("TeamIscritto nullo");
        return teamIscritto.getHackathon();
    }

    /**
     * Crea una nuova sottomissione per un team nello stato MANCANTE.
     * Viene chiamato automaticamente all'iscrizione del team all'hackathon.
     *
     * @param team il team per il quale creare la sottomissione
     * @return la sottomissione creata nello stato MANCANTE
     */
    public Sottomissione creaSottomissione(TeamIscritto team) {
        if (team == null) throw new IllegalArgumentException("Team nullo");
        return new Sottomissione(team);
    }
}