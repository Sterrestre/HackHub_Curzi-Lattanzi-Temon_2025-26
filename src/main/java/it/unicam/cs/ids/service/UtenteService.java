package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.repository.UtenteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;

    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    public List<Utente> cercaPerNickname(String nickname) {
        return utenteRepository.findByNicknameContainingIgnoreCase(nickname);
    }

    public List<Utente> cercaPerNomeOCognome(String query) {
        return utenteRepository
                .findByUtenteNomeContainingIgnoreCaseOrUtenteCognomeContainingIgnoreCase(query, query);
    }

    public Utente findById(String utenteId) {
        return utenteRepository.findById(utenteId)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
    }

    public void aggiornaIban(String utenteId, String nuovoIban) {
        Utente u = findById(utenteId);
        u.getConto().cambiaIban(nuovoIban);
        utenteRepository.save(u);
    }

    public void assegnaTeam(String utenteId, Team team) {
        Utente u = findById(utenteId);
        u.setTeam(team);
        utenteRepository.save(u);
    }
}
