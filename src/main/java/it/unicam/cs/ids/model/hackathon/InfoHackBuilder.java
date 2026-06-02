package it.unicam.cs.ids.model.hackathon;

import java.time.LocalDateTime;

public interface InfoHackBuilder {

    InfoHackBuilder regolamento(String regolamento);

    InfoHackBuilder dataInizio(LocalDateTime data);

    InfoHackBuilder dataFine(LocalDateTime data);

    InfoHackBuilder luogo(String luogo);

    InfoHackBuilder scadenzaIscrizioni(LocalDateTime scadenzaIscrizioni);

    InfoHackBuilder quotaIscrizione(double quotaIscrizione);

    InfoHackBuilder premio(double premio);

    InfoHackBuilder dimMaxTeam(int dimMaxTeam);

    InfoHackBuilder numMaxTeam(int numMaxTeam);

    InfoHack build();

}
