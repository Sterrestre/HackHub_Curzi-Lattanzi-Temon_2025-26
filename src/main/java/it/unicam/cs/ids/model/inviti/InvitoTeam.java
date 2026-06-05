package it.unicam.cs.ids.model.inviti;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.model.team.Team;
import jakarta.persistence.*;

/**
 * Invito inviato da un membro amministratore del team a un utente
 * esterno per unirsi al team.
 */
@Entity
@DiscriminatorValue("TEAM")
public class InvitoTeam extends Invito{
    /** Membro amministratore del team che ha inviato l'invito. */
    @ManyToOne
    @JoinColumn(name = "mittente_membro_id")
    private MembroTeam mittente;

    /** Team a cui si invita l'utente. */
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    // COSTRUTTORE PER JPA
    protected InvitoTeam() {}

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
