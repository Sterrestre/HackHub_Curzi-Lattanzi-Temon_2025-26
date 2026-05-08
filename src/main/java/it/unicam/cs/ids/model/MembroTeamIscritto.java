package it.unicam.cs.ids.model;

public class MembroTeamIscritto {

    private Utente utente;
    private TeamIscritto teamIscritto;
    private MetodoPagamento metodoPagamentoPremio;
    private boolean presente;
    private Mentore mentoreAssegnato;

    public MembroTeamIscritto(Utente utente,
                              TeamIscritto teamIscritto,
                              MetodoPagamento metodoPagamentoPremio,
                              boolean presente,
                              Mentore mentoreAssegnato) {

        this.utente = utente;
        this.teamIscritto = teamIscritto;
        this.metodoPagamentoPremio = metodoPagamentoPremio;
        this.presente = presente;
        this.mentoreAssegnato = mentoreAssegnato;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public TeamIscritto getTeamIscritto() {
        return teamIscritto;
    }

    public void setTeamIscritto(TeamIscritto teamIscritto) {
        this.teamIscritto = teamIscritto;
    }

    public MetodoPagamento getMetodoPagamentoPremio() {
        return metodoPagamentoPremio;
    }

    public void setMetodoPagamentoPremio(MetodoPagamento metodoPagamentoPremio) {
        this.metodoPagamentoPremio = metodoPagamentoPremio;
    }

    public boolean isPresente() {
        return presente;
    }

    public void setPresente(boolean presente) {
        this.presente = presente;
    }

    public Mentore getMentoreAssegnato() {
        return mentoreAssegnato;
    }

    public void setMentoreAssegnato(Mentore mentoreAssegnato) {
        this.mentoreAssegnato = mentoreAssegnato;
    }
}
}
