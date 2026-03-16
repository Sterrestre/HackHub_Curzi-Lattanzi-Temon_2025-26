package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.Invito;
import it.unicam.cs.ids.model.InvitoStaff;
import it.unicam.cs.ids.model.InvitoTeam;

import java.util.List;
import java.util.stream.Collectors;

public class MailCreator {

// INVITO MEMBRO STAFF O TEAM
    public static String creaOggettoInvito(Invito invito) {
        String oggetto = "Invito ";

        if (invito instanceof InvitoStaff staff)
            return oggetto.concat("- ruolo di" + staff.getRuolo() + " per l'hackathon " + staff.getHackathon());

        if (invito instanceof InvitoTeam team)
            return oggetto.concat("- team " + team.getMittente().getTeam());

        return oggetto;
    }

    public static String creaCorpoInvito(Invito invito) {
        String corpo = "Buongiorno " + invito.getDestinatario().getUtenteNome() + ",\n\n";

        if (invito instanceof InvitoStaff staff) {
            InfoHack infoHack = staff.getHackathon().getInfoHack();
            Utente mittente = staff.getMittente();
            return corpo.concat(mittente.getUtenteNome + " ti ha invitato come " + staff.getRuolo() + " per l'hackathon " + staff.getHackathon() + " da lui creato," +
                    "che si terrà dal " + infoHack.getDataInizio() + " al " + infoHack.getDataFine + " presso " + infoHack.getLuogo() + ". \n Per maggiori informazioni contatta l'organizzatore alla seguente mail "
                    + mittente.getUtenteEmail + ". \n\n ATTENZIONE! L'invito è valido fino a " +staff.getScadenza() ". Dopo tale data l'invito risulterà rifiutato e verrà eliminato");
        }

        if (invito instanceof InvitoTeam team)
            return corpo.concat(team.getMittente().getUtente.getUtenteNome() + " ti ha invitato a far parte del team " + team.getMittente().getTeam() + "! \n Ricorda che è possibile far parte di un solo team alla volta.");

        return corpo;
    }


// STAFF INCOMPLETO
    public static String creaOggettoOrganizzatore(Hackathon hack) {
        return "Inviti scaduti per l'hackathon " + hack.getNome();
    }

    public static String creaMessaggioStaffIncompleto(Hackathon hack, List<InvitoStaff> invitiScaduti) {
        String nomi = invitiScaduti.stream()
                .map(inv -> inv.getDestinatario().getUtenteNome())
                .collect(Collectors.joining(", "));

        return "Buongiorno " + hack.getOrganizzatore().getUtente().getUtenteNome() + ",\n\n" +
                "l'hackathon da te creato \"" + hack.getNome() + "\" risulta incompleto " +
                "perché i seguenti utenti non hanno accettato il tuo invito: " + nomi + ".\n\n" +
                "Invita nuovi utenti per poter procedere con la conferma dell'hackathon, " +
                "oppure elimina l'evento.";
    }
}
