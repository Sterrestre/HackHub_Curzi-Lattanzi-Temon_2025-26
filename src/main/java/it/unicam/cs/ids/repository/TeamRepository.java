package it.unicam.cs.ids.repository;

import it.unicam.cs.ids.model.team.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository JPA per la gestione della persistenza dei team.
 * Spring Data genera automaticamente le implementazioni dei metodi dichiarati.
 */
public interface TeamRepository extends JpaRepository<Team, String> {

    /**
     * Restituisce tutti i team il cui nome contiene la stringa specificata,
     * ignorando maiuscole/minuscole.
     */
    List<Team> findByNomeContainingIgnoreCase(String nome);

    boolean existsByNomeIgnoreCase(String nome);
}

