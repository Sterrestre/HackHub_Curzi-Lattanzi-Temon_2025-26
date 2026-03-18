package it.unicam.cs.ids.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public abstract class Hackathon {

    protected HackState state;              // stato corrente
    protected String nome;
    protected InfoHack infoHack;
    protected Stato stato;                  // enum: BOZZA, CONFERMATO, CONCLUSO
    protected List<RuoloPartecipazione> ruoli;
    protected int numTeamIscritti;
    protected StaffIncompleto staffIncompleto;
    protected Conto conto;
    protected List<Sottomissione> sottomissioni = new ArrayList<>();
    protected List<TeamIscritto> teamIscritti = new ArrayList<>();
    protected List<TeamIscritto> classifica = new ArrayList<>();
    protected TeamIscritto teamVincitore;
    protected boolean classificaConfermata = false;

    public Hackathon(InfoHack infoHack, String nome, Utente utente) {
        this.infoHack = infoHack;
        this.stato = Stato.BOZZA;
        this.numTeamIscritti = 0;
        this.nome = nome;
        this.ruoli = new ArrayList<>();
        RuoloPartecipazione organizzatore;
        RoleFactory factory = new RoleFactory();
        organizzatore = factory.assegnaOrganizzatore( utente, this);

        ruoli.add(organizzatore);
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


    public void aggiungiMentore(Utente mentore) {
        if (mentore == null) {
            throw new IllegalArgumentException("Il mentore non puo essere null");
        }
        RoleFactory factory = new RoleFactory();
        RuoloPartecipazione ruoloMentore = factory.assegnaMentore(mentore, this);
        state.aggiungiMentore(this, ruoloMentore);
    }


    public void aggiungiGiudice(Utente giudice) {
        if (giudice == null) {
            throw new IllegalArgumentException("Il giudice non puo essere null");
        }
        RoleFactory factory = new RoleFactory();
        RuoloPartecipazione ruoloGiudice = factory.assegnaGiudice(giudice, this);
        state.aggiungiMentore(this, ruoloGiudice);
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
        }
    }
    public Stato getStato() {
        return stato;
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

    public Utente getOrganizzatore() {
       return this.getRuoli().stream()
                .filter(rp -> rp.getTipoRuolo() == RuoliStaff.ORGANIZZATORE)
                .map(RuoloPartecipazione::getUtente)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Hackathon senza organizzatore"));
    }

    public List<TeamIscritto> getTeamIscritti() {
        return this.teamIscritti;
    }

    private TeamIscritto getTeamById(Long id) {
        return getTeamIscritti().stream()
                .filter(t -> t.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Team non trovato"));
    }

    public List<Sottomissione> getSottomissioniValutate() {
        return sottomissioni.stream().filter(Sottomissione::isValutata).toList();
    }

    public List<TeamIscritto> calcolaClassificaPreliminare() {

        List<TeamIscritto> team = getTeamIscritti();

        team.sort((t1, t2) -> {
            double v1 = t1.getSottomissione().getVotazione();
            double v2 = t2.getSottomissione().getVotazione();
            return Double.compare(v2, v1); // ordine decrescente
        });

        this.classifica = new ArrayList<>(team);
        return this.classifica;
    }

    public List<TeamIscritto> getClassificaCorrente() {
        return this.classifica;
    }

    public Sottomissione getDettagliSottomissione(long id) {
        return sottomissioni.stream()
                .filter(s -> s.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Sottomissione non trovata"));
    }

    public String getGiudizioSottomissione(long id) {
        return getDettagliSottomissione(id).getGiudizio();
    }

    public void aggiornaClassifica(List<Long> nuovoOrdineTeam) {
        if (classificaConfermata)
            throw new IllegalStateException("La classifica è già stata confermata");

        List<TeamIscritto> nuova = new ArrayList<>();

        for (Long id : nuovoOrdineTeam) {
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
}
