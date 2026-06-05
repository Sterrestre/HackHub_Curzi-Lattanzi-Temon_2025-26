package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.MetodoPagamento;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.MembroTeamIscritto;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.NotificationService;
import it.unicam.cs.ids.service.TeamService;
import org.springframework.stereotype.Service;

@Service
public class TeamHandler {

    private final TeamService teamService;
    private final NotificationService notificationService;
    private final InvitiHandler invitiHandler;

    public TeamHandler(TeamService teamService, NotificationService notificationService, InvitiHandler invitiHandler) {
        this.teamService = teamService;
        this.notificationService = notificationService;
        this.invitiHandler = invitiHandler;
    }

    /**
     * Crea un nuovo team con il nome specificato e l'utente amministratore. L'utente amministratore diventa automaticamente membro del team.
     * @param nome del team che si vuole creare
     * @param amministratore l'utente che crea il team. Non deve appartenere a nessun altro team.
     * @return il nuovo team
     */
    public Team creaTeam(String nome, Utente amministratore) {
        //TODO implementare il catch dell'eccezione che implementi l'opt team != null
        if (amministratore.getTeam() != null) {
            throw new IllegalStateException("L'utente è già membro di un team");
        };

        //TODO form con i dati da inserire
        //Validazioni di input
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome team obbligatorio");
        }

        Team team = teamService.creaTeam(nome, amministratore);
        System.out.println("Team " + team.getNome() + " creato con successo. Amministratore: " + amministratore.getNickname());
        return team;
    }

    //TODO la parte che riguarda il membro può essere delegata a MembriTeamHandler
    public MembroTeam aggiungiMembroTeam(Team team, Utente utente, boolean amministratore) {
        MembroTeam nuovo = teamService.aggiungiMembro(team, utente, amministratore);
    //    notificationService.inviaNotificaAggiuntaMemebro(?);
        return nuovo;
    }


    public TeamIscritto iscriviTeam(Team team, Hackathon hackathon, MembroTeam amministratore) {
        return new TeamIscritto(team, hackathon, amministratore.getUtente());
    }


    /**
     * Invita un utente a unirsi al team. L'utente riceverà una notifica e potrà accettare o rifiutare l'invito.
     * @param admin il membro del team che vuole mandare l'invito
     * @param utente l'utente che si vuole invitare ad unirsi al team. L'utente può far già parte di un altro team o meno.
     * @throws IllegalStateException se l'utente è già membro del team.
     * @return messaggio di conferma dell'invio.
     */
    public String invitaUtente(MembroTeam admin, Utente utente) {
        Team newTeam = admin.getTeam();
        if (utente.getTeam() == newTeam) {
            throw new IllegalStateException("L'utente è già membro del team");
        }
        invitiHandler.creaInvitoTeam(admin, utente, newTeam);
        return "L'utente "+utente.getNickname()+" è stato invitato al team "+admin.getTeam().getNome();
    }

    public MembroTeamIscritto iscriviMembroTeam(MembroTeam membTeam, Hackathon hackathon) {
        // TODO implementa sequence corrispondente. Return alla cazzo per farlo star buono, CORREGGI
        return new MembroTeamIscritto(membTeam.getUtente(), new TeamIscritto(membTeam.getTeam(), hackathon, membTeam.getUtente()), MetodoPagamento.NON_SELEZIONATO);
    }
}