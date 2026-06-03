package it.unicam.cs.ids.model.team;

import it.unicam.cs.ids.model.Utente;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.UUID;

@Entity
public class MembroTeam {

    @Id
    private String membroTeamId;

    @ManyToOne(optional = false)
    private Utente utente;

    @ManyToOne(optional = false)
    private Team team;

    private boolean amministratore = false;

    // COSTRUTTORE JPA
    protected MembroTeam() {}

    /**
     * Costruttore per creare un nuovo membro del team. L'utente e il team non possono essere null.
     * @param utente
     * @param team
     */
    public MembroTeam(Utente utente, Team team) {
        if (utente == null) throw new NullPointerException("L'utente non puo' essere null!");
        if (team == null) throw new NullPointerException("Il team non puo' essere null!");

        this.utente = utente;
        this.team = team;
        this.membroTeamId = UUID.randomUUID().toString();
    }

    public String getMembroTeamId() {
        return membroTeamId;
    }

    public Utente getUtente() {
        return  this.utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isAmministratore() {
        return this.amministratore;
    }

    public void setAmministratore(boolean amministratore) {
        this.amministratore = amministratore;
    }
}
