package it.unicam.cs.ids.handler;

import it.unicam.cs.ids.model.Sottomissione;
import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.hackathon.*;
import it.unicam.cs.ids.model.staff.*;
import it.unicam.cs.ids.model.team.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static it.unicam.cs.ids.model.staff.RuoliStaff.ORGANIZZATORE;

/**
 * Handler per la logica di dominio degli hackathon.
 * Non gestisce persistenza — quella è responsabilità di HackathonService.
 */
@Service
public class HackHandler {

    private final RoleFactory roleFactory;
    private final TeamHandler teamHandler;
    private final InvitiHandler invitiHandler;
    private final SottomissioneHandler sottomissioneHandler;

    public HackHandler(RoleFactory roleFactory, TeamHandler teamHandler,
                       InvitiHandler invitiHandler, SottomissioneHandler sottomissioneHandler) {
        this.roleFactory = roleFactory;
        this.teamHandler = teamHandler;
        this.invitiHandler = invitiHandler;
        this.sottomissioneHandler = sottomissioneHandler;
    }

    /**
     * Crea un nuovo hackathon e registra l'organizzatore.
     * La persistenza è delegata a HackathonService.
     */
    public Hackathon creaHackathon(Utente utente, String nome, InfoHack info) {
        if (!utente.isMembroDiStaff()) {
            throw new IllegalArgumentException("Solo i membri di staff possono creare un hackathon");
        }
        Hackathon hackathon = new Hackathon(info, nome);
        roleFactory.creaERegistraRuolo(ORGANIZZATORE, utente, hackathon);
        return hackathon;
    }

    /** Conferma un hackathon delegando al suo stato corrente. */
    public void confermaHackathon(Hackathon hackathon) {
        hackathon.conferma();
    }

    /** Elimina un hackathon delegando al suo stato corrente. */
    public void eliminaHackathon(Hackathon hackathon) {
        hackathon.elimina();
    }

    /** Cambia lo stato di un hackathon. */
    public void cambiaStato(Hackathon hackathon, Stato nuovoStato) {
        switch (nuovoStato) {
            case BOZZA -> hackathon.cambiaStato(new BozzaState(invitiHandler));
            case CONFERMATO -> hackathon.cambiaStato(new ConfermatoState(hackathon));
            case IN_CORSO -> hackathon.cambiaStato(new InCorsoState());
            case CONCLUSO -> hackathon.cambiaStato(new ConclusoState());
        }
    }

    public void setInfo(Hackathon hackathon, InfoHack info) {
        hackathon.setInfo(info);
    }

    public void modificaRegolamento(Hackathon hackathon, String nuovoRegolamento) {
        hackathon.modificaRegolamento(nuovoRegolamento);
    }

    public void modificaDataInizio(Hackathon hackathon, LocalDateTime nuovaData) {
        hackathon.modificaDataInizio(nuovaData);
    }

    public void aggiungiMentore(Utente mentore, Hackathon hackathon) {
        hackathon.aggiungiMentore(mentore);
    }

    public void aggiungiGiudice(Utente giudice, Hackathon hackathon) {
        hackathon.aggiungiGiudice(giudice);
    }

    public static void setStaffIncompleto(Hackathon hackathon, StaffIncompleto staffIncompleto) {
        hackathon.setStaffIncompleto(staffIncompleto);
    }

    public void invitaStaff(Hackathon hackathon, Utente utente, RuoliStaff tipoRuolo) {
        hackathon.invitaStaff(utente, tipoRuolo);
    }

    public List<Sottomissione> getSottomissioniValutate(Hackathon hackathon) {
        return hackathon.getSottomissioniValutate();
    }

    public List<TeamIscritto> calcolaClassificaPreliminare(Hackathon hackathon) {
        return hackathon.calcolaClassificaPreliminare();
    }

    public List<TeamIscritto> visualizzaClassifica(Hackathon hackathon) {
        return hackathon.getClassificaCorrente();
    }

    public Sottomissione getDettagli(Hackathon hackathon, String sottomissioneID) {
        return hackathon.getDettagliSottomissione(sottomissioneID);
    }

    public String getGiudizio(Hackathon hackathon, Sottomissione sottomissione) {
        return hackathon.getGiudizioSottomissione(sottomissione);
    }

    public void aggiornaClassifica(Hackathon hackathon, List<String> nuovoOrdineTeam) {
        hackathon.aggiornaClassifica(nuovoOrdineTeam);
    }

    public void confermaClassifica(Hackathon hackathon) {
        hackathon.confermaClassifica();
    }

    public void setTeamVincitore(Hackathon hackathon, TeamIscritto team) {
        hackathon.setTeamVincitore(team);
    }

    public double getPremioInDenaro(Hackathon hackathon) {
        return hackathon.getPremioInDenaro();
    }

    public void concludiHackathon(Hackathon hackathon) {
        hackathon.cambiaStato(new ConclusoState());
    }

    public List<TeamIscritto> getTeamIscritti(Hackathon hackathon) {
        return hackathon.getTeamIscritti();
    }

    public List<MembroTeamIscritto> getMembriIscritti(TeamIscritto team) {
        return team.getElencoIscritti();
    }

    public void validaPresenze(Hackathon hackathon) {
        hackathon.validaPresenze();
    }

    public void salvaPresenze(Hackathon hackathon) {
        hackathon.salvaPresenze();
    }

    public void assegnaMentore(TeamIscritto team, Mentore mentore) {
        team.setMentoreAssegnato(mentore);
        mentore.aggiungiTeamAssegnato(team);
    }

    public void impostaPresenza(MembroTeamIscritto membro, boolean presenza) {
        membro.setPresenza(presenza);
    }

    public void setPresente(MembroTeamIscritto membro, boolean presente) {
        membro.setPresenza(presente);
    }

    public boolean validaDati(Hackathon hackathon, String teamID,
                              String tipoIntervento, String motivazione) {
        boolean teamEsiste = hackathon.getTeamIscritti().stream()
                .anyMatch(t -> t.getTeam().getTeamID().equals(teamID));
        if (!teamEsiste) return false;
        if (tipoIntervento == null || tipoIntervento.isBlank()) return false;
        if (motivazione == null || motivazione.isBlank()) return false;
        return true;
    }

    public void applicaPenalizzazione(Hackathon hackathon, String teamID,
                                      String tipoIntervento, String motivazione) {
        hackathon.applicaPenalizzazione(teamID, tipoIntervento, motivazione);
    }

    // Implementazione del CU "IscriviTeam" - It2
    public TeamIscritto iscriviTeam(Team team, Hackathon hackathon, MembroTeam amministratore) {

        // Prerequisito: controllo che il membro del team che vuole iscrivere il team sia un amministratore
        if (!amministratore.isAmministratore()) {
            throw new DomainException("Solo un amministratore del team può iscrivere il team all'hackathon");
        }

        // Controllo che l'hackathon sia confermato: non si può iscrivere un team
        // a un hackathon ancora in bozza, concluso o già in corso
        if (hackathon.getStato() != Stato.CONFERMATO) {
            throw new DomainException("Non è possibile iscrivere un team a un hackathon che non è confermato");
        }

        // Controllo che il team non sia già iscritto
        boolean giaIscritto = hackathon.getTeamIscritti().stream()
                .anyMatch(t -> t.getTeam().getTeamID().equals(team.getTeamID()));
        if (giaIscritto) {
            throw new DomainException("Il team è già iscritto a questo hackathon");
        }

        // Controllo che la scadenza per iscriversi non sia stata raggiunta
        LocalDateTime scadIscr = hackathon.getInfoHack().getScadenzaIscrizioni();
        if (scadIscr.isBefore(LocalDateTime.now())) {
            throw new Scaduto();
        }

        // Controllo che ci sia ancora posto per un altro team
        int numMax = hackathon.getInfoHack().getNumMaxTeam();
        if (hackathon.getTeamIscritti().size() >= numMax) {
            throw new HackCompleto();
        }

        // Controllo che nessun membro del team sia organizzatore o giudice dell'hackathon
        for (MembroTeam m : team.getMembri()) {
            boolean isOrganizzatoreOGiudice = m.getUtente().getRuoli().stream()
                    .filter(r -> r.getHackathon().equals(hackathon))
                    .anyMatch(r -> r instanceof Organizzatore || r instanceof Giudice);
            if (isOrganizzatoreOGiudice) {
                throw new DomainException("Il team non può iscriversi: almeno un membro è organizzatore o giudice");
            }
        }

        // Controllo se c'è una quota di iscrizione
        if (hackathon.getInfoHack().getQuotaIscrizione() > 0) {
            // TODO futuri: collegare il sistema di pagamento
            throw new DomainException("Funzionalità non implementata: pagamento richiesto");
        }

        // Iscrivo il team
        TeamIscritto newTeam = teamHandler.iscriviTeam(team, hackathon, amministratore);
        hackathon.aggiungiTeamIscritto(newTeam);
        newTeam.setSottomissione(sottomissioneHandler.creaSottomissione(newTeam));
        return newTeam;
    }

    // Implementazione del CU "Iscriviti" - It2
    public String iscriviMembroteam(MembroTeam membTeam, Hackathon hackathon) {

        // Controllo che la data di scadenza delle iscrizioni non sia stata superata
        LocalDateTime scad = hackathon.getInfoHack().getScadenzaIscrizioni();
        if (scad.isBefore(LocalDateTime.now())) {
            return "Iscrizioni chiuse";
        }

        // Controllo che il num max di iscritti per quel team non sia già stato raggiunto
        int dimMax = hackathon.getInfoHack().getDimMaxTeam();
        Team team = membTeam.getTeam();
        int iscritti = hackathon.getTeamIscritti().stream()
                .filter(t -> t.getTeam().equals(team))
                .mapToInt(TeamIscritto::getNumIscritti)
                .sum();
        if (iscritti >= dimMax) {
            return "Numero massimo di iscritti per il team raggiunto";
        }

        // Controllo che non sia un mentore
        if (membTeam.getUtente().getRuoli().stream()
                .anyMatch(r -> r.getHackathon() == hackathon && r.getTipoRuolo() == RuoliStaff.MENTORE)) {
            return "I mentori non possono iscriversi come membri del team";
        }

        // Delego la logica di iscrizione al teamHandler
        MembroTeamIscritto nuovoIscritto = teamHandler.iscriviMembroTeam(membTeam, hackathon);
        return nuovoIscritto != null ? "Iscrizione avvenuta con successo" : "Iscrizione fallita";
    }
}