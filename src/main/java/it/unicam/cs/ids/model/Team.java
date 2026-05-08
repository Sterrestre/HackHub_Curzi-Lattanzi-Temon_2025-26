package it.unicam.cs.ids.model;

import java.util.ArrayList;
import java.util.List;

public class Team {

    private String teamID;
    private String nome;
    private List<MembroTeam> membri = new ArrayList<>();

    public Team(String teamID, String nome) {
        this.teamID = teamID;
        this.nome = nome;
    }

    public String getTeamID() {
        return teamID;
    }

    public void setTeamID(String teamID) {
        this.teamID = teamID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<MembroTeam> getMembri() {
        return membri;
    }

    public void aggiungiMembro(Utente utente) {
        MembroTeam nuovo = new MembroTeam(utente, this, false);
        membri.add(nuovo);
    }

    public void rimuoviMembro(MembroTeam membroTeam) {
        membri.remove(membroTeam);
    }

    public void rendiAmministratore(MembroTeam membroTeam) {
        for (MembroTeam m : membri) {
            m.setAmministratore(false);
        }
        membroTeam.setAmministratore(true);
    }
}
