package it.unicam.cs.ids.model.team;

import it.unicam.cs.ids.model.*;
import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.staff.Mentore;
import it.unicam.cs.ids.model.MetodoPagamento;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TeamIscritto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    private Team team;

    @ManyToOne
    private Hackathon hackathon;
    @ManyToOne
    private Utente amministratore;
    @OneToMany(mappedBy = "teamIscritto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MembroTeamIscritto> elencoIscritti = new ArrayList<>();
    @OneToOne(cascade = CascadeType.ALL)
    private Sottomissione sottomissione;
    @ManyToOne
    private Mentore mentoreAssegnato;
    @OneToMany(mappedBy = "teamIscritto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RichiestaSupporto> richiesteSupporto = new ArrayList<>();

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "teamID", column = @Column(name = "pen_team_id")),
            @AttributeOverride(name = "tipoIntervento", column = @Column(name = "pen_tipo")),
            @AttributeOverride(name = "motivazione", column = @Column(name = "pen_motivazione"))
    })
    private Penalizzazione penalizzazione;

    // COSTRUTTORE PER JPA
    protected TeamIscritto() {
    }

    public TeamIscritto(Team team, Hackathon hackathon, Utente amministratore) {
        this.team = team;
        this.hackathon = hackathon;
        this.amministratore = amministratore;
        MembroTeamIscritto membroAdmin = new MembroTeamIscritto(amministratore, this, MetodoPagamento.NON_SELEZIONATO);
        this.elencoIscritti.add(membroAdmin);
    }


    public Team getTeam() {
        return team;
    }

    public Hackathon getHackathon() {
        return hackathon;
    }

    public Utente getAmministratore() {
        return amministratore;
    }

    public List<MembroTeamIscritto> getElencoIscritti() {
        return elencoIscritti;
    }

    public Sottomissione getSottomissione() {
        return sottomissione;
    }

    public void aggiungiIscritto(MembroTeamIscritto membro) {
        if (!elencoIscritti.contains(membro)) {
            elencoIscritti.add(membro);
        }
    }

    public void rimuoviIscritto(MembroTeamIscritto membro) {
        elencoIscritti.remove(membro);
    }

    public void inviaSottomissione(Sottomissione sottomissione) {
        this.sottomissione = sottomissione;
    }

    public int getNumIscritti() {
        return elencoIscritti.size();
    }

    public void setSottomissione(Sottomissione sottomissione) {
        this.sottomissione = sottomissione;
    }

    public String getTeamIscrittoId() {
        return id;
    }

    public String getId() {
        return id;
    }

    public Mentore getMentoreAssegnato() {
        return mentoreAssegnato;
    }

    public void setMentoreAssegnato(Mentore mentore) {
        this.mentoreAssegnato = mentore;
    }

    public List<RichiestaSupporto> getRichiesteSupporto() {
        return richiesteSupporto;
    }

    public void aggiungiRichiestaSupporto(RichiestaSupporto richiesta) {
        richiesteSupporto.add(richiesta);
    }

    public void segnaRichiestaVisualizzata(RichiestaSupporto richiesta) {
        richiesta.modificaStato(true);
    }
}
