package it.unicam.cs.ids.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.unicam.cs.ids.dto.CreaHackathonRequest;
import it.unicam.cs.ids.dto.HackathonDTO;
import it.unicam.cs.ids.dto.InfoHackDTO;
import it.unicam.cs.ids.handler.HackHandler;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.TeamService;
import it.unicam.cs.ids.service.UtenteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Test a livello di controller: verifica il comportamento HTTP reale
 * dell'endpoint (status code, corpo della risposta JSON), non la logica
 * interna (quella e' gia' testata separatamente su HackHandler).
 * Le dipendenze del controller sono sostituite con dei mock (@MockBean):
 * qui non ci interessa cosa fanno service/handler, solo che il controller
 * traduca correttamente le loro risposte in risposte HTTP.
 */
@WebMvcTest(HackController.class)
class HackControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private HackathonService hackathonService;
    @MockBean
    private UtenteService utenteService;
    @MockBean
    private HackHandler hackHandler;
    @MockBean
    private TeamService teamService;

    @Test
    void getAllRestituisceLaListaDegliHackathonInFormatoJson() throws Exception {
        Hackathon hackathon = mock(Hackathon.class);
        when(hackathon.getHackathonID()).thenReturn("hack-1");
        when(hackathon.getNome()).thenReturn("Hackathon di prova");
        when(hackathon.getStato()).thenReturn(Stato.CONFERMATO);
        when(hackathon.getTeamIscritti()).thenReturn(List.of());
        when(hackathon.getInfoHack()).thenReturn(
                new it.unicam.cs.ids.model.hackathon.InfoHackBuilderImpl()
                        .luogo("Camerino")
                        .numMaxTeam(8)
                        .build()
        );

        when(hackathonService.getTutti()).thenReturn(List.of(hackathon));

        mockMvc.perform(get("/hackathon/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("hack-1"))
                .andExpect(jsonPath("$[0].nome").value("Hackathon di prova"))
                .andExpect(jsonPath("$[0].stato").value("CONFERMATO"));
    }

    @Test
    void getAllRestituisceListaVuotaSeNonCiSonoHackathon() throws Exception {
        when(hackathonService.getTutti()).thenReturn(List.of());

        mockMvc.perform(get("/hackathon/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getByIdRestituisceBadRequestSeLIdNonEsiste() throws Exception {
        when(hackathonService.getHackathonByID("id-inesistente"))
                .thenThrow(new IllegalArgumentException("Hackathon non trovato"));

        mockMvc.perform(get("/hackathon/id-inesistente"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creaRestituisceLHackathonAppenaCreato() throws Exception {
        Utente organizzatore = mock(Utente.class);
        Hackathon hackathonCreato = mock(Hackathon.class);

        when(hackathonCreato.getHackathonID()).thenReturn("hack-nuovo");
        when(hackathonCreato.getNome()).thenReturn("Nuovo hackathon");
        when(hackathonCreato.getStato()).thenReturn(Stato.BOZZA);
        when(hackathonCreato.getTeamIscritti()).thenReturn(List.of());
        when(hackathonCreato.getInfoHack()).thenReturn(
                new it.unicam.cs.ids.model.hackathon.InfoHackBuilderImpl()
                        .luogo("Camerino")
                        .numMaxTeam(8)
                        .build()
        );

        when(utenteService.findById("org-1")).thenReturn(organizzatore);
        when(hackathonService.creaHackathon(
                org.mockito.ArgumentMatchers.eq(organizzatore),
                org.mockito.ArgumentMatchers.eq("Nuovo hackathon"),
                org.mockito.ArgumentMatchers.any()
        )).thenReturn(hackathonCreato);

        CreaHackathonRequest richiesta = new CreaHackathonRequest(
                "org-1",
                "Nuovo hackathon",
                "Regolamento",
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(35),
                LocalDateTime.now().plusDays(20),
                "Camerino",
                0,
                500,
                8,
                4
        );

        mockMvc.perform(post("/hackathon/crea")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(richiesta)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("hack-nuovo"))
                .andExpect(jsonPath("$.nome").value("Nuovo hackathon"));
    }

    @Test
    void creaRestituisceBadRequestSeLOrganizzatoreNonEsiste() throws Exception {
        when(utenteService.findById("org-inesistente"))
                .thenThrow(new IllegalArgumentException("Utente non trovato"));

        CreaHackathonRequest richiesta = new CreaHackathonRequest(
                "org-inesistente",
                "Nuovo hackathon",
                "Regolamento",
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now().plusDays(35),
                LocalDateTime.now().plusDays(20),
                "Camerino",
                0,
                500,
                8,
                4
        );

        mockMvc.perform(post("/hackathon/crea")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(richiesta)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void confermaRestituisceOkQuandoLaConfermaVaABuonFine() throws Exception {
        mockMvc.perform(post("/hackathon/hack-1/conferma"))
                .andExpect(status().isOk());
    }

    @Test
    void confermaRestituisceBadRequestSeLoStatoNonEValido() throws Exception {
        org.mockito.Mockito.doThrow(new IllegalStateException("Stato non valido"))
                .when(hackathonService).aggiornaStato("hack-1", Stato.CONFERMATO);

        mockMvc.perform(post("/hackathon/hack-1/conferma"))
                .andExpect(status().isBadRequest());
    }
}