package it.unicam.cs.ids.model.hackathon;

import java.time.LocalDateTime;

// Pattern: Builder
public class InfoHackBuilderImpl implements InfoHackBuilder {

    private final InfoHack info = new InfoHack();

    @Override
    public InfoHackBuilder regolamento(String regolamento) {
        info.setRegolamento(regolamento);
        return this;
    }

    @Override
    public InfoHackBuilder dataInizio(LocalDateTime data) {
        info.setDataInizio(data);
        return this;
    }

    @Override
    public InfoHackBuilder dataFine(LocalDateTime data) {
        info.setDataFine(data);
        return this;
    }

    @Override
    public InfoHackBuilder luogo(String luogo) {
        info.setLuogo(luogo);
        return this;
    }

    @Override
    public InfoHackBuilder scadenzaIscrizioni(LocalDateTime scadenza) {
        info.setScadenzaIscrizioni(scadenza);
        return this;
    }

    @Override
    public InfoHackBuilder quotaIscrizione(double quota) {
        info.setQuotaIscrizione(quota);
        return this;
    }

    @Override
    public InfoHackBuilder premio(double premio) {
        info.setPremio(premio);
        return this;
    }

    @Override
    public InfoHackBuilder dimMaxTeam(int dim) {
        info.setDimMaxTeam(dim);
        return this;
    }

    @Override
    public InfoHackBuilder numMaxTeam(int num) {
        info.setNumMaxTeam(num);
        return this;
    }

    @Override
    public InfoHack build() {
        return info;
    }
}