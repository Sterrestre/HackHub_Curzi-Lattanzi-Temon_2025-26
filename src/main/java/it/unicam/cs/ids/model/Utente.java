package it.unicam.cs.ids.model;

import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.staff.RuoloPartecipazione;
import it.unicam.cs.ids.model.team.Team;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Utente {
    @Id
    private String utenteID;

    private String utenteNome;
    private String utenteCognome;
    private String utenteEmail;
    private String nickname;
    private String biografia;
    private LocalDate dataDiNascita;

    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RuoloPartecipazione> ruoli = new ArrayList<>();

    private boolean membroDiStaff = false;

    @ManyToOne(optional = true)
    private Team team = null;

    @Embedded
    private Conto conto;

    // COSTRUTTORE PER JPA
    protected Utente() {}

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

    public RuoloPartecipazione getRuoloHackathon(Hackathon hackathon) {
        if (!this.isMembroDiStaff()) {
            RuoloPartecipazione ruolo = this.getRuoli().stream()
                    .filter(r -> r.getHackathon().equals(hackathon))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("L'utente non ha un ruolo per questo hackathon"));
            return ruolo;
        }
        return null;
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

    // Metodo helper per ricavare i permessi
    private List<RuoloPartecipazione> getRuoliPerHackathon(Hackathon h) {
        return ruoli.stream()
                .filter(r -> r.getHackathon().equals(h))
                .toList();
    }

    ///
    /// METODI PER RICAVARE I PERMESSI IN BASE ALL'HACKATHON
    ///
    public boolean puoValutare(Hackathon h) {
        return getRuoliPerHackathon(h).stream()
                .anyMatch(RuoloPartecipazione::puoValutare);
    }

    public boolean puoPenalizzare(Hackathon h) {
        return getRuoliPerHackathon(h).stream()
                .anyMatch(RuoloPartecipazione::puoPenalizzare);
    }

    public boolean puoMentorare(Hackathon h) {
        return getRuoliPerHackathon(h).stream()
                .anyMatch(RuoloPartecipazione::puoMentorare);
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
