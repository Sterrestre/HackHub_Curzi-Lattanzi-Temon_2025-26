package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.Team;
import it.unicam.cs.ids.repository.UtenteRepository;
import it.unicam.cs.ids.dto.RegistraUtenteRequest;
import java.util.UUID;
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

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Utente findByEmail(String email) {
        Utente utente = utenteRepository.findByUtenteEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utente non trovato"));
        org.hibernate.Hibernate.initialize(utente.getRuoli());
        return utente;
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

    public Utente registra(RegistraUtenteRequest req) {

        if (utenteRepository.existsByNicknameIgnoreCase(req.nickname())) {
            throw new IllegalArgumentException("Esiste già un utente con questo nickname");
        }
        if (utenteRepository.existsByUtenteEmailIgnoreCase(req.email())) {
            throw new IllegalArgumentException("Esiste già un utente con questa email");
        }

        Utente utente = new Utente(
                UUID.randomUUID().toString(),
                req.nome(),
                req.cognome(),
                req.email(),
                req.nickname(),
                req.biografia(),
                req.dataDiNascita()
        );
        utente.setMembroDiStaff(req.membroDiStaff());
        return utenteRepository.save(utente);
    }

    public List<Utente> findAll() {
        return utenteRepository.findAll();
    }
}
