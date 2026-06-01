package it.unicam.cs.ids.model.hackathon;

import it.unicam.cs.ids.model.*;
import it.unicam.cs.ids.model.staff.RoleFactory;
import it.unicam.cs.ids.model.staff.RuoliStaff;
import it.unicam.cs.ids.model.staff.RuoloPartecipazione;
import it.unicam.cs.ids.model.team.MembroTeamIscritto;
import it.unicam.cs.ids.model.team.TeamIscritto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static it.unicam.cs.ids.model.staff.RuoliStaff.ORGANIZZATORE;

public class Hackathon {

    private final String id;

    private HackState state;              // stato corrente
    protected String nome;
    protected InfoHack infoHack;
    public Stato stato;                  // enum: BOZZA, CONFERMATO, CONCLUSO
    public List<RuoloPartecipazione> ruoli; // public per essere visibile dalla roleFactory
    protected int numTeamIscritti;
    protected StaffIncompleto staffIncompleto;
    protected Conto conto;
    protected List<Sottomissione> sottomissioni = new ArrayList<>();
    protected List<TeamIscritto> teamIscritti = new ArrayList<>();
    protected List<TeamIscritto> classifica = new ArrayList<>();
    protected TeamIscritto teamVincitore;
    protected boolean classificaConfermata = false;
    protected List<Penalizzazione> penalizzazioni = new ArrayList<>();

    public Hackathon(InfoHack infoHack, String nome) {
        this.id = UUID.randomUUID().toString();
        this.infoHack = infoHack;
        this.numTeamIscritti = 0;
        this.nome = nome;
        this.ruoli = new ArrayList<>();
    }

    public void setState(HackState newState) {
        this.state = newState;
    }


    public void setInfoHack(InfoHack infoHack) {
        this.infoHack = infoHack;
    }

    public void setInfo(InfoHack info) {
        state.setInfoHack(this, info);
    }

    public void modificaRegolamento(String nuovoRegolamento) {
        state.modificaRegolamento(this, nuovoRegolamento);
    }

    public void modificaDataInizio(LocalDateTime nuovaData) {
        state.modificaDataInizio(this, nuovaData);
    }

    public void modificaDataFine(LocalDateTime nuovaData) {
        state.modificaDataFine(this, nuovaData);
    }

    public void modificaLuogo(String nuovoLuogo) {
        state.modificaLuogo(this, nuovoLuogo);
    }

    public void modificaScadenzaIscrizioni(LocalDateTime nuovaScadenza) {
        state.modificaScadenzaIscrizioni(this, nuovaScadenza);
    }

    public void modificaQuotaIscrizione(double nuovaQuota) {
        state.modificaQuotaIscrizione(this, nuovaQuota);
    }

    public void modificaPremio(double nuovoPremio) {
        state.modificaPremio(this, nuovoPremio);
    }

    public void modificaDimMaxTeam(int nuovaDim) {
        state.modificaDimMaxTeam(this, nuovaDim);
    }

    public void modificaNumMaxTeam(int nuovoNum) {
        state.modificaNumMaxTeam(this, nuovoNum);
    }


    // metodi da inserire in hackhandler?
    public void aggiungiMentore(Utente mentore) {
        if (mentore == null) {
            throw new IllegalArgumentException("Il mentore non puo essere null");
        }
        RoleFactory factory = new RoleFactory();
        RuoloPartecipazione ruoloMentore = factory.assegnaMentore(mentore, this);
        state.aggiungiMentore(this, ruoloMentore.getUtente());
    }


    public void aggiungiGiudice(Utente giudice) {
        if (giudice == null) {
            throw new IllegalArgumentException("Il giudice non puo essere null");
        }
        RoleFactory factory = new RoleFactory();
        RuoloPartecipazione ruoloGiudice = factory.assegnaGiudice(giudice, this);
        state.aggiungiGiudice(this, ruoloGiudice.getUtente());
    }


    public void invitaStaff(Utente utente, RuoliStaff tipoRuolo) {
        state.invitaStaff(this, utente, tipoRuolo);
    }

    public void elimina() {
        state.eliminaHackathon(this);
    }

    public void conferma() {
        state.confermaHackathon(this);
    }

    public void cambiaStato(HackState nuovoStato) {
        this.state = nuovoStato;
        // Aggiorna anche l'enum Stato in base al tipo di stato
        if (nuovoStato instanceof BozzaState) {
            this.stato = Stato.BOZZA;
        } else if (nuovoStato instanceof ConfermatoState) {
            this.stato = Stato.CONFERMATO;
        } else if (nuovoStato instanceof ConclusoState) {
            this.stato = Stato.CONCLUSO;
        } else if (nuovoStato instanceof InCorsoState) {
            this.stato = Stato.IN_CORSO;
        }
    }
    public Stato getStato() {
        return stato;
    }

    public String getNome() {
        return nome;
    }

    public String getHackathonID() {
        return this.id;
    }

    public InfoHack getInfoHack() {
        return this.infoHack;
    }

    public List<RuoloPartecipazione> getRuoli() {
        return this.ruoli;
    }

    public void setStaffIncompleto(StaffIncompleto staffIncompleto) {
        this.staffIncompleto = staffIncompleto;
    }

    public StaffIncompleto getStaffIncompleto() { return this.staffIncompleto;}

    public Utente getOrganizzatore() {
       return this.getRuoli().stream()
                .filter(rp -> rp.getTipoRuolo() == ORGANIZZATORE)
                .map(RuoloPartecipazione::getUtente)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Hackathon senza organizzatore"));
    }

    public List<TeamIscritto> getTeamIscritti() {
        return this.teamIscritti;
    }

    private TeamIscritto getTeamById(String id) {
        return getTeamIscritti().stream()
                .filter(t -> t.getTeam().getTeamID().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
    }

    public List<Sottomissione> getSottomissioniValutate() {
        return sottomissioni.stream().filter(Sottomissione::isValutata).toList();
    }

    public List<TeamIscritto> calcolaClassificaPreliminare() {

        List<TeamIscritto> team = getTeamIscritti();

        team.sort((t1, t2) -> {
            double v1 = t1.getSottomissione().getValutazione().getVoto();
            double v2 = t2.getSottomissione().getValutazione().getVoto();
            return Double.compare(v2, v1); // ordine decrescente
        });

        this.classifica = new ArrayList<>(team);
        return this.classifica;
    }

    public List<TeamIscritto> getClassificaCorrente() {
        return this.classifica;
    }

    public Sottomissione getDettagliSottomissione(String id) {
        return sottomissioni.stream()
                .filter(s -> Objects.equals(s.getId(), id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Sottomissione non trovata"));
    }

    public String getGiudizioSottomissione(Sottomissione id) {
        return getDettagliSottomissione(id.getSottomissioneID()).getGiudizio();
    }

    public void aggiornaClassifica(List<String> nuovoOrdineTeam) {
        if (classificaConfermata)
            throw new IllegalStateException("La classifica è già stata confermata");

        List<TeamIscritto> nuova = new ArrayList<>();

        for (String id : nuovoOrdineTeam) {
            nuova.add(getTeamById(id));
        }

        this.classifica = nuova;
    }

    public void confermaClassifica() {
        this.classificaConfermata = true;
    }

    public void setTeamVincitore(TeamIscritto team) {
        this.teamVincitore = team;
    }

    public double getPremioInDenaro() {
        return this.infoHack.getPremio();
    }

    public void validaPresenze() {
        // esempio di validazione minima
        for (TeamIscritto team : teamIscritti) {
            for (MembroTeamIscritto m : team.getElencoIscritti()) {
                if (!m.isPresente()) {
                    throw new IllegalStateException("Presenze non valide per il team: " + team.getTeam().getNome());
                }
            }
        }
    }

    public void salvaPresenze() {
    }

    public List<TeamIscritto> getTeamAssegnati(String mentoreID) {
        return teamIscritti.stream()
                .filter(team -> team.getMentoreAssegnato() != null)
                .filter(team -> team.getMentoreAssegnato().getMentoreID() == mentoreID)
                .toList();
    }

    public void applicaPenalizzazione(long teamID, String tipoIntervento, String motivazione) {
        Penalizzazione p = new Penalizzazione(teamID, tipoIntervento, motivazione);
        penalizzazioni.add(p);
    }

}
