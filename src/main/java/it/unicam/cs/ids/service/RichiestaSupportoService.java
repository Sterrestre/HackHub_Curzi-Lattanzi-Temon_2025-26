package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.RichiestaSupporto;
import it.unicam.cs.ids.model.team.TeamIscritto;
import it.unicam.cs.ids.repository.HackathonRepository;
import org.springframework.stereotype.Service;

/**
 * Service per la gestione delle richieste di supporto.
 * Le richieste vivono nel contesto di un TeamIscritto, che a sua volta
 * vive in un Hackathon, quindi si recuperano tramite HackathonRepository.
 */
@Service
public class RichiestaSupportoService {

    private final HackathonRepository hackathonRepository;

    public RichiestaSupportoService(HackathonRepository hackathonRepository) {
        this.hackathonRepository = hackathonRepository;
    }

    public RichiestaSupporto findById(String richiestaId) {
        return hackathonRepository.findAll().stream()
                .flatMap(h -> h.getTeamIscritti().stream())
                .flatMap(ti -> ti.getRichiesteSupporto().stream())
                .filter(r -> r.getRichiestaSuppID().equals(richiestaId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Richiesta di supporto non trovata"));
    }

    public RichiestaSupporto salva(RichiestaSupporto richiesta) {
        TeamIscritto team = richiesta.getTeamIscritto();
        team.aggiungiRichiestaSupporto(richiesta);
        hackathonRepository.save(team.getHackathon());
        return richiesta;
    }
}
