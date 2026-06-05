package it.unicam.cs.ids.model.inviti;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;

public class InvitoTeam extends Invito{
    private final MembroTeam mittente;
    private final Team team;

    public InvitoTeam(MembroTeam mittente, Utente destinatario, Team team) {
        super(destinatario);
        this.mittente = mittente;
        this.team = team;
    }


    public MembroTeam getMittente() {
        return mittente;
    }

    public Team getTeam() {
        return team;
    }
}
