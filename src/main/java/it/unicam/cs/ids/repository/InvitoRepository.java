package it.unicam.cs.ids.repository;

import it.unicam.cs.ids.model.inviti.Invito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository JPA per la gestione della persistenza degli inviti.
 * Spring Data genera automaticamente le implementazioni dei metodi dichiarati.
 */
public interface InvitoRepository extends JpaRepository<Invito, String> {

    /**
     * Restituisce tutti gli inviti destinati a un utente specifico.
     * Il nome del metodo rispecchia il percorso: destinatario → utenteID (campo in Utente).
     */
    List<Invito> findByDestinatarioUtenteID(String utenteID);

}
