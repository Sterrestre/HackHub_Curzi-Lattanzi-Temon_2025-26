package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.InfoHack;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.model.staff.RoleFactory;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test unitari per le regole di dominio del metodo HackHandler.iscriviTeam.
 * Non serve un database: Team, Hackathon e MembroTeam sono oggetti finti (mock)
 * di cui controlliamo solo il comportamento dei metodi che il codice usa davvero.
 */
@ExtendWith(MockitoExtension.class)
class HackHandlerTest {

    @Mock
    private RoleFactory roleFactory;
    @Mock
    private TeamHandler teamHandler;
    @Mock
    private InvitiHandler invitiHandler;
    @Mock
    private SottomissioneHandler sottomissioneHandler;

    private HackHandler creaHandler() {
        return new HackHandler(roleFactory, teamHandler, invitiHandler, sottomissioneHandler);
    }

    @Test
    void soloUnAmministratorePuoIscrivereIlTeam() {
        HackHandler handler = creaHandler();

        Team team = mock(Team.class);
        Hackathon hackathon = mock(Hackathon.class);
        MembroTeam nonAdmin = mock(MembroTeam.class);
        when(nonAdmin.isAmministratore()).thenReturn(false);

        assertThatThrownBy(() -> handler.iscriviTeam(team, hackathon, nonAdmin))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("amministratore");
    }

    @Test
    void nonSiPuoIscrivereUnTeamAUnHackathonNonConfermato() {
        HackHandler handler = creaHandler();

        Team team = mock(Team.class);
        Hackathon hackathon = mock(Hackathon.class);
        MembroTeam admin = mock(MembroTeam.class);

        when(admin.isAmministratore()).thenReturn(true);
        when(hackathon.getStato()).thenReturn(Stato.BOZZA);

        assertThatThrownBy(() -> handler.iscriviTeam(team, hackathon, admin))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("confermato");
    }

    @Test
    void unTeamGiaIscrittoNonPuoIscriversiDiNuovo() {
        HackHandler handler = creaHandler();

        Team team = mock(Team.class);
        Hackathon hackathon = mock(Hackathon.class);
        MembroTeam admin = mock(MembroTeam.class);
        TeamIscritto iscrizioneEsistente = mock(TeamIscritto.class);
        Team stessoTeam = mock(Team.class);

        when(admin.isAmministratore()).thenReturn(true);
        when(hackathon.getStato()).thenReturn(Stato.CONFERMATO);
        when(team.getTeamID()).thenReturn("team-1");
        when(stessoTeam.getTeamID()).thenReturn("team-1");
        when(iscrizioneEsistente.getTeam()).thenReturn(stessoTeam);
        when(hackathon.getTeamIscritti()).thenReturn(List.of(iscrizioneEsistente));

        assertThatThrownBy(() -> handler.iscriviTeam(team, hackathon, admin))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("già iscritto");
    }

    @Test
    void scadenzaIscrizioniSuperataLanciaScaduto() {
        HackHandler handler = creaHandler();

        Team team = mock(Team.class);
        Hackathon hackathon = mock(Hackathon.class);
        MembroTeam admin = mock(MembroTeam.class);
        InfoHack info = mock(InfoHack.class);

        when(admin.isAmministratore()).thenReturn(true);
        when(hackathon.getStato()).thenReturn(Stato.CONFERMATO);
        when(hackathon.getTeamIscritti()).thenReturn(Collections.emptyList());
        when(hackathon.getInfoHack()).thenReturn(info);
        when(info.getScadenzaIscrizioni()).thenReturn(LocalDateTime.now().minusDays(1));

        assertThatThrownBy(() -> handler.iscriviTeam(team, hackathon, admin))
                .isInstanceOf(Scaduto.class);
    }

    @Test
    void hackathonAlCompletoLanciaHackCompleto() {
        HackHandler handler = creaHandler();

        Team team = mock(Team.class);
        Hackathon hackathon = mock(Hackathon.class);
        MembroTeam admin = mock(MembroTeam.class);
        InfoHack info = mock(InfoHack.class);
        TeamIscritto altraIscrizione = mock(TeamIscritto.class);
        Team altroTeam = mock(Team.class);

        when(admin.isAmministratore()).thenReturn(true);
        when(hackathon.getStato()).thenReturn(Stato.CONFERMATO);
        when(team.getTeamID()).thenReturn("team-1");
        when(altroTeam.getTeamID()).thenReturn("team-2");
        when(altraIscrizione.getTeam()).thenReturn(altroTeam);
        when(hackathon.getTeamIscritti()).thenReturn(List.of(altraIscrizione));
        when(hackathon.getInfoHack()).thenReturn(info);
        when(info.getScadenzaIscrizioni()).thenReturn(LocalDateTime.now().plusDays(5));
        when(info.getNumMaxTeam()).thenReturn(1); // gia' un team iscritto su un massimo di 1

        assertThatThrownBy(() -> handler.iscriviTeam(team, hackathon, admin))
                .isInstanceOf(HackCompleto.class);
    }

    @Test
    void iscrizioneValidaVieneCompletataConSuccesso() {
        HackHandler handler = creaHandler();

        Team team = mock(Team.class);
        Hackathon hackathon = mock(Hackathon.class);
        MembroTeam admin = mock(MembroTeam.class);
        InfoHack info = mock(InfoHack.class);
        TeamIscritto risultatoAtteso = mock(TeamIscritto.class);

        when(admin.isAmministratore()).thenReturn(true);
        when(hackathon.getStato()).thenReturn(Stato.CONFERMATO);
        when(hackathon.getTeamIscritti()).thenReturn(Collections.emptyList());
        when(hackathon.getInfoHack()).thenReturn(info);
        when(info.getScadenzaIscrizioni()).thenReturn(LocalDateTime.now().plusDays(5));
        when(info.getNumMaxTeam()).thenReturn(10);
        when(team.getMembri()).thenReturn(Collections.emptyList());
        when(info.getQuotaIscrizione()).thenReturn(0.0);
        when(teamHandler.iscriviTeam(team, hackathon, admin)).thenReturn(risultatoAtteso);

        TeamIscritto risultato = handler.iscriviTeam(team, hackathon, admin);

        assertThat(risultato).isEqualTo(risultatoAtteso);
    }

    @Test
    void unTeamNonPuoIscriversiSeUnMembroEGiudiceDellHackathon() {
        HackHandler handler = creaHandler();

        Team team = mock(Team.class);
        Hackathon hackathon = mock(Hackathon.class);
        MembroTeam admin = mock(MembroTeam.class);
        InfoHack info = mock(InfoHack.class);
        MembroTeam membroGiudice = mock(MembroTeam.class);
        it.unicam.cs.ids.model.Utente utenteGiudice = mock(it.unicam.cs.ids.model.Utente.class);

        it.unicam.cs.ids.model.staff.Giudice ruoloGiudice =
                new it.unicam.cs.ids.model.staff.Giudice(utenteGiudice, hackathon);

        when(admin.isAmministratore()).thenReturn(true);
        when(hackathon.getStato()).thenReturn(Stato.CONFERMATO);
        when(hackathon.getTeamIscritti()).thenReturn(Collections.emptyList());
        when(hackathon.getInfoHack()).thenReturn(info);
        when(info.getScadenzaIscrizioni()).thenReturn(LocalDateTime.now().plusDays(5));
        when(info.getNumMaxTeam()).thenReturn(10);
        when(team.getMembri()).thenReturn(List.of(membroGiudice));
        when(membroGiudice.getUtente()).thenReturn(utenteGiudice);
        when(utenteGiudice.getRuoli()).thenReturn(List.of(ruoloGiudice));

        assertThatThrownBy(() -> handler.iscriviTeam(team, hackathon, admin))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("organizzatore o giudice");
    }
}