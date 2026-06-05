package it.unicam.cs.ids.repository;

import it.unicam.cs.ids.model.inviti.Invito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public interface InvitoRepository extends JpaRepository<Invito, String> {

    // Viene creato in automatico:
//    List<Invito> findAll();

    List<Invito> findByDestinatarioId(String destinatarioId);

    default List<Invito> findInvitiPerDestinatario(String destinatarioId) {
        List<Invito> inviti = findByDestinatarioId(destinatarioId);
        return inviti != null ? inviti : new ArrayList<>();
    }
}
