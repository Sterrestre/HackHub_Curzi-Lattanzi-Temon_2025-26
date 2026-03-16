package it.unicam.cs.ids.model;

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
}
