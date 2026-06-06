package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.Utente;

/**
 * DTO per la rappresentazione di un utente nella response.
 * Espone solo i campi necessari senza esporre il modello direttamente.
 */
public record UtenteDTO(
        String id,
        String nome,
        String cognome,
        String email,
        String nickname,
        String biografia,
        boolean membroDiStaff
) {
    public static UtenteDTO from(Utente u) {
        return new UtenteDTO(
                u.getUtenteID(),
                u.getUtenteNome(),
                u.getUtenteCognome(),
                u.getUtenteEmail(),
                u.getNickname(),
                u.getBiografia(),
                u.isMembroDiStaff()
        );
    }
}