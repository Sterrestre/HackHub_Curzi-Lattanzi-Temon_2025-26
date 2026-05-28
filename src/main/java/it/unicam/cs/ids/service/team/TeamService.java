package it.unicam.cs.ids.service.team;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import org.springframework.stereotype.Service;

@Service
public class TeamService {

    public Team creaTeam(String nome, Utente amministratore) {
        Team team = new Team(nome);
        MembroTeam creatore = team.aggiungiMembro(amministratore);
        //TODO dove sta rendiAmministratore?
        team.rendiAmministratore(creatore);
        return team;
    }

    public MembroTeam aggiungiMembro(Team team, Utente utente) {
        return team.aggiungiMembro(utente);
    }

    public void rimuoviMembro(Team team, MembroTeam membro) {
        team.rimuoviMembro(membro);
    }

    public void rendiAmministratore(Team team, MembroTeam membro) {
        team.rendiAmministratore(membro);
    }


}
