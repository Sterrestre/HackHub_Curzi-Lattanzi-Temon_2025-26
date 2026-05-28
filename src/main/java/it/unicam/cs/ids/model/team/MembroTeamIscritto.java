package it.unicam.cs.ids.model.team;

import it.unicam.cs.ids.model.MetodoPagamento;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.staff.Mentore;

public class MembroTeamIscritto {
    private TeamIscritto teamIscritto;
    private Utente utente;
    private boolean presente = false;
    private MetodoPagamento metodoPagamentoPremio;

    public MembroTeamIscritto(Utente utente, TeamIscritto teamIscritto, MetodoPagamento metodoPagamentoPremio) {
        if (utente == null) throw new  IllegalArgumentException("Il membro del team che si vuole iscrivere all'hackathon non può essere null.");
        if (metodoPagamentoPremio == null) throw new IllegalArgumentException("Metodo di pagamento non valido");
        this.utente = utente;
        this.teamIscritto = teamIscritto;
        this.metodoPagamentoPremio = metodoPagamentoPremio;
    }

    public Utente getUtente(){
        return this.utente;
    }

    public TeamIscritto getTeamIscritto(){
        return this.teamIscritto;
    }

    public MetodoPagamento getMetodoPagamentoPremio(){
        return this.metodoPagamentoPremio;
    }

    public void cambiaMetodoPagamento(MetodoPagamento metodoPagamento){
        this.metodoPagamentoPremio = metodoPagamento;
    }

    public void setPresenza(boolean presenza) {
        this.presente = presenza;
    }

    public boolean isPresente() {
        return presente;
    }
}


