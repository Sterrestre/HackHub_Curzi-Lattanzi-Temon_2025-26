package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.RichiestaSupporto;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.HackathonService;
import it.unicam.cs.ids.service.MailSender;
import it.unicam.cs.ids.service.SistemaCall;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Gestisce le operazioni legate alle richieste di supporto e alle call.
 */
@Service
public class SupportoHandler {

    private final SistemaCall sistemaCall;
    private final HackathonService hackathonService;

    public SupportoHandler(SistemaCall sistemaCall, HackathonService hackathonService) {
        this.sistemaCall = sistemaCall;
        this.hackathonService = hackathonService;
    }

    /**
     * Genera un collegamento tramite il sistema esterno (Calendar)
     */
    public String generaCollegamento(SistemaCall sistemaCall) {
        return sistemaCall.generaCollegamento(LocalDateTime.now());
    }


    /**
     * Risponde a una richiesta di supporto
     */
    public void rispondiAllaRichiesta(RichiestaSupporto richiesta) {
        richiesta.modificaStato(true);
        System.out.println("Risposta alla richiesta del team: " + richiesta.getDettagli());
    }

    /**
     * Invia una proposta di call al team
     * DA CONTROLLARE SE NECESSARIO
     */
   // public void inviaPropostaCall(RichiestaSupporto richiesta, LocalDateTime dataOra) {

        // 1. genera link call
      //  String link = SistemaCall.generaCollegamento(dataOra);

   // }


    /**
     * Invia una proposta di call al team
     */
   public void richiestaProponiCall(LocalDateTime dataOra, RichiestaSupporto richiesta, LocalDateTime fineHackathon) {
    if (dataOra == null || fineHackathon == null || !dataOra.isBefore(fineHackathon)) {
        throw new IllegalArgumentException(
                "La data e ora della call devono essere prima della fine dell'hackathon."
        );
    }

    String link = sistemaCall.generaCollegamento(dataOra);
    MailSender.inviaPropostaTeam(richiesta, dataOra, link);// simula l'invio con la mail

}

    /**
     * Annulla proposta call
     */
    public void annullaCall() {
        System.out.println("Proposta call annullata.");
    }

    /**
     * Verifica che la data sia valida
     */
    public boolean inviaDatiCall(LocalDateTime dataOra, LocalDateTime fineHackathon) {
        return dataOra.isBefore(fineHackathon);
    }

    public List<TeamIscritto> getTeamAssegnati(String hackathonID, String mentoreID) {
        Hackathon h = hackathonService.getHackathonByID(hackathonID);
        return h.getTeamAssegnati(mentoreID);
    }

    public Map<RichiestaSupporto, Boolean> getRichiesteSupporto(String teamID, String hackathonID) {
        Hackathon h = hackathonService.getHackathonByID(hackathonID);
        TeamIscritto team = h.getTeamIscritti().stream()
                .filter(t -> t.getTeam().getTeamID().equals(teamID))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato: " + teamID));

        return team.getRichiesteSupporto();
    }
}


