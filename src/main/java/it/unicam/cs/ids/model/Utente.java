package it.unicam.cs.ids.model;

import it.unicam.cs.ids.model.staff.RuoloPartecipazione;
import it.unicam.cs.ids.model.team.Team;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Utente {

    private String utenteID;
    private String utenteNome;
    private String utenteCognome;
    private String utenteEmail;
    private String nickname;
    private String biografia;
    private LocalDate dataDiNascita;
    private List<RuoloPartecipazione> ruoli = new ArrayList<>();
    private boolean membroDiStaff = false;
    private Team team = null;
    private Conto conto;

    public Utente(String utenteID, String utenteNome, String utenteCognome, String utenteEmail, String nickname, String biografia, LocalDate dataDiNascita) {
        this.utenteID = utenteID;
        this.utenteNome = utenteNome;
        this.utenteCognome = utenteCognome;
        this.utenteEmail = utenteEmail;
        this.nickname = nickname;
        this.biografia = biografia;
        this.dataDiNascita = dataDiNascita;
    }

    public String getUtenteID() {
        return utenteID;
    }

    public String getUtenteNome() {
        return utenteNome;
    }

    public String getUtenteCognome() {
        return utenteCognome;
    }

    public String getUtenteEmail() {
        return utenteEmail;
    }

    public List<RuoloPartecipazione> getRuoli() {
        return ruoli;
    }

    public String getNickname() {
        return nickname;
    }

    public String getBiografia() {
        return biografia;
    }

    public LocalDate getDataDiNascita() {
        return dataDiNascita;
    }

    public void addRuolo(RuoloPartecipazione ruolo) {
        if(!ruoli.contains(ruolo)){
            ruoli.add(ruolo);
        }
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public Conto getConto() {
        return conto;
    }

    public boolean isMembroDiStaff() {
        return membroDiStaff;
    }

    public void setMembroDiStaff(boolean membroDiStaff) {
        this.membroDiStaff = membroDiStaff;
    }
}
