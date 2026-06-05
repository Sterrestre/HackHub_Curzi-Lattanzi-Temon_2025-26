package it.unicam.cs.ids.repository;

import it.unicam.cs.ids.model.hackathon.Hackathon;
import it.unicam.cs.ids.model.hackathon.Stato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HackathonRepository extends JpaRepository<Hackathon, String> {

    List<Hackathon> findByNomeContainingIgnoreCase(String nome);

    List<Hackathon> findByStato(Stato stato);
}

