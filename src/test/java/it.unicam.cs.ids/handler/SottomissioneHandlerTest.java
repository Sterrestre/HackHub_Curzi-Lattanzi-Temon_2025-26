package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.model.team.TeamIscritto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test unitari per le regole di dominio del metodo
 * SottomissioneHandler.caricaSottomissione, in particolare la regola secondo
 * cui una sottomissione puo' essere caricata solo mentre l'hackathon e' IN_CORSO.
 */
@ExtendWith(MockitoExtension.class)
class SottomissioneHandlerTest {

    private final SottomissioneHandler handler = new SottomissioneHandler();

    @Test
    void nonSiPuoCaricareUnaSottomissioneSeLHackathonNonEInCorso() {
        TeamIscritto teamIscritto = mock(TeamIscritto.class);
        Hackathon hackathon = mock(Hackathon.class);

        when(teamIscritto.getHackathon()).thenReturn(hackathon);
        when(hackathon.getStato()).thenReturn(Stato.CONFERMATO); // non ancora IN_CORSO

        assertThatThrownBy(() ->
                handler.caricaSottomissione(teamIscritto, "Titolo", "Descrizione", "https://esempio.it/repo")
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("in corso");
    }

    @Test
    void nonSiPuoCaricareUnaSottomissioneConTitoloVuoto() {
        TeamIscritto teamIscritto = mock(TeamIscritto.class);

        assertThatThrownBy(() ->
                handler.caricaSottomissione(teamIscritto, "", "Descrizione", "https://esempio.it/repo")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Titolo");
    }

    @Test
    void unaSottomissioneVieneCaricataCorrettamenteQuandoLHackathonEInCorso() {
        TeamIscritto teamIscritto = mock(TeamIscritto.class);
        Hackathon hackathon = mock(Hackathon.class);

        when(teamIscritto.getHackathon()).thenReturn(hackathon);
        when(hackathon.getStato()).thenReturn(Stato.IN_CORSO);
        when(teamIscritto.getSottomissione()).thenReturn(null); // nessuna sottomissione precedente

        handler.caricaSottomissione(teamIscritto, "Il mio progetto", "Descrizione", "https://esempio.it/repo");

        // Se non e' stata lanciata alcuna eccezione, il metodo ha impostato
        // correttamente una nuova sottomissione sul team iscritto.
        org.mockito.Mockito.verify(teamIscritto).setSottomissione(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void nonSiPuoValutareSeLUtenteNonNeHaIPermessi() {
        Utente utente = mock(Utente.class);
        Hackathon hackathon = mock(Hackathon.class);
        TeamIscritto teamIscritto = mock(TeamIscritto.class);

        when(utente.puoValutare(hackathon)).thenReturn(false);

        assertThatThrownBy(() ->
                handler.valutaSottomissione(utente, hackathon, teamIscritto, 8, "Ottimo lavoro")
        )
                .isInstanceOf(SecurityException.class);
    }

    @Test
    void nonSiPuoAssegnareUnVotoFuoriRange() {
        Utente utente = mock(Utente.class);
        Hackathon hackathon = mock(Hackathon.class);
        TeamIscritto teamIscritto = mock(TeamIscritto.class);

        when(utente.puoValutare(hackathon)).thenReturn(true);

        assertThatThrownBy(() ->
                handler.valutaSottomissione(utente, hackathon, teamIscritto, 15, "Voto non valido")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Voto");
    }

    @Test
    void nonSiPuoValutareSenzaGiudizio() {
        Utente utente = mock(Utente.class);
        Hackathon hackathon = mock(Hackathon.class);
        TeamIscritto teamIscritto = mock(TeamIscritto.class);

        when(utente.puoValutare(hackathon)).thenReturn(true);

        assertThatThrownBy(() ->
                handler.valutaSottomissione(utente, hackathon, teamIscritto, 8, "")
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Giudizio");
    }

    @Test
    void nonSiPuoValutareUnaSottomissioneMancante() {
        Utente utente = mock(Utente.class);
        Hackathon hackathon = mock(Hackathon.class);
        TeamIscritto teamIscritto = mock(TeamIscritto.class);

        when(utente.puoValutare(hackathon)).thenReturn(true);
        when(teamIscritto.getSottomissione()).thenReturn(null);

        assertThatThrownBy(() ->
                handler.valutaSottomissione(utente, hackathon, teamIscritto, 8, "Ottimo lavoro")
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("mancante");
    }

    @Test
    void nonSiPuoValutareDueVolteLaStessaSottomissione() {
        Utente utente = mock(Utente.class);
        Hackathon hackathon = mock(Hackathon.class);
        TeamIscritto teamIscritto = mock(TeamIscritto.class);
        it.unicam.cs.ids.model.Sottomissione sottomissione =
                mock(it.unicam.cs.ids.model.Sottomissione.class);

        when(utente.puoValutare(hackathon)).thenReturn(true);
        when(teamIscritto.getSottomissione()).thenReturn(sottomissione);
        when(sottomissione.getStatoSottomissione())
                .thenReturn(it.unicam.cs.ids.model.StatoSottomissione.VALUTATA);

        assertThatThrownBy(() ->
                handler.valutaSottomissione(utente, hackathon, teamIscritto, 8, "Ottimo lavoro")
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("già stata valutata");
    }

    @Test
    void unaValutazioneValidaVieneApplicataConSuccesso() {
        Utente utente = mock(Utente.class);
        Hackathon hackathon = mock(Hackathon.class);
        TeamIscritto teamIscritto = mock(TeamIscritto.class);
        it.unicam.cs.ids.model.Sottomissione sottomissione =
                mock(it.unicam.cs.ids.model.Sottomissione.class);

        when(utente.puoValutare(hackathon)).thenReturn(true);
        when(teamIscritto.getSottomissione()).thenReturn(sottomissione);
        when(sottomissione.getStatoSottomissione())
                .thenReturn(it.unicam.cs.ids.model.StatoSottomissione.CARICATA);

        handler.valutaSottomissione(utente, hackathon, teamIscritto, 8, "Ottimo lavoro");

        org.mockito.Mockito.verify(sottomissione).setValutazione(org.mockito.ArgumentMatchers.any());
        org.mockito.Mockito.verify(sottomissione)
                .setStatoSottomissione(it.unicam.cs.ids.model.StatoSottomissione.VALUTATA);
    }
}