package it.unicam.cs.ids.model.team;

import it.unicam.cs.ids.model.MetodoPagamento;
import it.unicam.cs.ids.model.Utente;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * Rappresenta un membro di un team iscritto a un hackathon.
 * Tiene traccia della presenza e del metodo di pagamento per il premio.
 */
@Entity
public class MembroTeamIscritto {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "team_iscritto_id")
    private TeamIscritto teamIscritto;

    @ManyToOne
    private Utente utente;

    private boolean presente = false;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamentoPremio;

    // COSTRUTTORE PER JPA
    protected MembroTeamIscritto() {}

    public MembroTeamIscritto(Utente utente, TeamIscritto teamIscritto, MetodoPagamento metodoPagamentoPremio) {
        if (utente == null) throw new IllegalArgumentException("Il membro del team che si vuole iscrivere all'hackathon non può essere null.");
        if (metodoPagamentoPremio == null) throw new IllegalArgumentException("Metodo di pagamento non valido");
        this.id = UUID.randomUUID().toString();
        this.utente = utente;
        this.teamIscritto = teamIscritto;
        this.metodoPagamentoPremio = metodoPagamentoPremio;
    }

    public Utente getUtente() {
        return this.utente;
    }

    public TeamIscritto getTeamIscritto() {
        return this.teamIscritto;
    }

    public MetodoPagamento getMetodoPagamentoPremio() {
        return this.metodoPagamentoPremio;
    }

    public void cambiaMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamentoPremio = metodoPagamento;
    }

    public void setPresenza(boolean presenza) {
        this.presente = presenza;
    }

    public boolean isPresente() {
        return presente;
    }
}