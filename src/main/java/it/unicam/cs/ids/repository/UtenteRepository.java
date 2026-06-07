package it.unicam.cs.ids.repository;

import it.unicam.cs.ids.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UtenteRepository extends JpaRepository<Utente, String> {

    // NOTA: Spring Data genere automaticamente:
    // - findById(String id))
    // - save(Utente u)
    // - deleteById(String id)
    // - findAll()


    // Esempi di query utili (opzionali)
    Optional<Utente> findByUtenteEmail(String email);

    List<Utente> findByNicknameContainingIgnoreCase(String nickname);

    List<Utente> findByUtenteNomeContainingIgnoreCase(String nome);

    List<Utente> findByUtenteCognomeContainingIgnoreCase(String cognome);

    List<Utente> findByUtenteNomeContainingIgnoreCaseOrUtenteCognomeContainingIgnoreCase(
            String nome, String cognome);

    boolean existsByUtenteEmailIgnoreCase(String email);
    boolean existsByNicknameIgnoreCase(String nickname);
}

