package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.MembroTeamIscritto;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.service.NotificationService;
import it.unicam.cs.ids.service.TeamService;
import org.springframework.stereotype.Service;

/**
 * Handler per la logica di dominio dei team.
 */
@Service
public class TeamHandler {

    private final NotificationService notificationService;
    private final InvitiHandler invitiHandler;
    private final MembriTeamHandler membriTeamHandler;

    public TeamHandler(NotificationService notificationService, InvitiHandler invitiHandler, MembriTeamHandler membriTeamHandler) {
        this.notificationService = notificationService;
        this.invitiHandler = invitiHandler;
        this.membriTeamHandler = membriTeamHandler;
    }

    /**
     * Crea un nuovo team con il nome specificato e l'utente amministratore.
     * L'utente amministratore diventa automaticamente membro del team.
     *
     * @param nome           il nome del team
     * @return il nuovo team
     * @throws IllegalStateException    se l'utente è già membro di un team
     * @throws IllegalArgumentException se il nome è null o vuoto
     */
    public Team creaTeam(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome team obbligatorio");
        }
        return new Team(nome);
    }

    /**
     * Aggiunge un membro al team.
     *
     * @param team           il team a cui aggiungere il membro
     * @param utente         l'utente da aggiungere
     * @param amministratore true se il nuovo membro deve essere amministratore
     * @return il nuovo MembroTeam creato
     */
    public MembroTeam aggiungiMembroTeam(Team team, Utente utente, boolean amministratore) {
        if (utente.getTeam() != null) {
            throw new IllegalStateException("L'utente è già membro di un team");
        };
        MembroTeam nuovoMembro = team.aggiungiMembro(utente);
        if (amministratore) {
            team.rendiAmministratore(nuovoMembro);
        }
        return nuovoMembro;
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

    /**
     * Iscrive un membro del team a un hackathon delegando a MembriTeamHandler.
     *
     * @param membTeam  il membro del team da iscrivere
     * @param hackathon l'hackathon a cui iscriversi
     * @return il nuovo MembroTeamIscritto creato
     */
    public MembroTeamIscritto iscriviMembroTeam(MembroTeam membTeam, Hackathon hackathon) {
        return membriTeamHandler.iscriviMembroTeam(membTeam, hackathon);
    }
}