package it.unicam.cs.ids.repository;

import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.Stato;
import it.unicam.cs.ids.model.team.TeamIscritto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repository JPA per la gestione della persistenza degli hackathon.
 * Contiene anche le query per i TeamIscritto, che vivono nel contesto di un hackathon.
 * Spring Data genera automaticamente le implementazioni dei metodi dichiarati.
 */
public interface HackathonRepository extends JpaRepository<Hackathon, String> {

    List<Hackathon> findByNomeContainingIgnoreCase(String nome);

    List<Hackathon> findByStato(Stato stato);

    @Query("SELECT ti FROM TeamIscritto ti WHERE ti.id = :teamIscrittoId")
    Optional<TeamIscritto> findTeamIscrittoById(@Param("teamIscrittoId") String teamIscrittoId);
}

