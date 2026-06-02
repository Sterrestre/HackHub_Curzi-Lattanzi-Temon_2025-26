package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.StatoSottomissione;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.model.Valutazione;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;

/**
 * Handler per la gestione delle sottomissioni dei team agli hackathon.
 * Fornisce metodi per il caricamento, la valutazione e il recupero delle sottomissioni,
 * nonché per la creazione e la visualizzazione delle informazioni correlate.
 */
@Service
public class SottomissioneHandler {

    /**
     * Carica o sostituisce la sottomissione di un team iscritto.
     * Se la sottomissione non esiste, ne crea una nuova e la associa al team.
     * Se esiste già, la sostituisce con il nuovo file.
     *
     * @param teamIscritto il team iscritto al quale associare la sottomissione
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

        // Nel flow/sequence: se non esiste -> crea, se esiste -> sostituisce
        Sottomissione sottomissione = teamIscritto.getSottomissione();
        if (sottomissione == null) {
            sottomissione = new Sottomissione(teamIscritto.toString());
            teamIscritto.setSottomissione(sottomissione);
        }

        sottomissione.setFile(sottomissioneFile);
        sottomissione.setDataCaricamento(LocalDateTime.now());
        sottomissione.setStatoSottomissione(StatoSottomissione.CARICATA);
    }

    /**
     * Valuta una sottomissione di un team iscritto.
     * La sottomissione deve essere stata precedentemente caricata e non deve essere nello stato MANCANTE.
     *
     * @param teamIscritto il team iscritto cui appartiene la sottomissione
     * @param valutazione l'oggetto Valutazione contenente voto e giudizio
     * @throws IllegalArgumentException se teamIscritto o valutazione sono nulli
     * @throws IllegalStateException se la sottomissione non è stata caricata o è nello stato MANCANTE
     */
    public void valutaSottomissione(TeamIscritto teamIscritto, Valutazione valutazione) {
        if (teamIscritto == null) {
            throw new IllegalArgumentException("TeamIscritto nullo");
        }

        Sottomissione sottomissione = teamIscritto.getSottomissione();
        if (sottomissione == null || sottomissione.getStatoSottomissione() == StatoSottomissione.MANCANTE) {
            // coerente al senso del caso d’uso: non puoi valutare se non c'è una submission caricata
            throw new IllegalStateException("Impossibile valutare: sottomissione mancante");
        }

        if (valutazione == null) {
            throw new IllegalArgumentException("Valutazione nulla");
        }

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
     * La sottomissione viene inizializzata nello stato MANCANTE.
     *
     * @param team il team per il quale creare la sottomissione
     * @param hack l'hackathon a cui appartiene la sottomissione
     * @param titolo il titolo della sottomissione (non utilizzato nella creazione attuale)
     * @param descrizione la descrizione della sottomissione (non utilizzato nella creazione attuale)
     * @param s l'ID della sottomissione
     * @return la sottomissione creata nello stato MANCANTE
     * @throws IllegalArgumentException se team, hack o s sono nulli/vuoti
     */
    public Sottomissione creaSottomissione(Team team, Hackathon hack, String titolo, String descrizione, String s) {
        if (team == null) {
            throw new IllegalArgumentException("Team nullo");
        }
        if (hack == null) {
            throw new IllegalArgumentException("Hackathon nullo");
        }
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("Id sottomissione nullo");
        }

        Sottomissione sottomissione = new Sottomissione(s);
        sottomissione.setStatoSottomissione(StatoSottomissione.MANCANTE);
        return sottomissione;
    }

    /**
     * Crea una sottomissione valutata con voto e giudizio.
     * Questo metodo crea una sottomissione nello stato VALUTATA con voto e giudizio forniti.
     *
     * @param s l'ID della sottomissione
     * @param voto il voto numerico della sottomissione (verrà arrotondato a intero)
     * @param giudizio il giudizio testuale della sottomissione
     * @return la sottomissione creata e valutata
     * @throws IllegalArgumentException se s è nullo o vuoto
     */
    public Sottomissione valutaSottomissione(String s, double voto, String giudizio) {
        if (s == null || s.isBlank()) {
            throw new IllegalArgumentException("Id sottomissione nullo");
        }

        Sottomissione sottomissione = new Sottomissione(s);
        sottomissione.setValutazione(new Valutazione((int) Math.round(voto), giudizio));
        sottomissione.setStatoSottomissione(StatoSottomissione.VALUTATA);
        return sottomissione;
    }

    /**
     * Recupera una sottomissione per ID.
     * Questo metodo crea e restituisce una nuova istanza di Sottomissione con l'ID fornito.
     *
     * @param sottomissioneId l'ID della sottomissione da recuperare
     * @return una nuova istanza di Sottomissione con l'ID fornito
     * @throws IllegalArgumentException se sottomissioneId è nullo o vuoto
     */
    public Sottomissione getSottomissione(String sottomissioneId) {
        if (sottomissioneId == null || sottomissioneId.isBlank()) {
            throw new IllegalArgumentException("Id sottomissione nullo");
        }

        return new Sottomissione(sottomissioneId);
    }
}
