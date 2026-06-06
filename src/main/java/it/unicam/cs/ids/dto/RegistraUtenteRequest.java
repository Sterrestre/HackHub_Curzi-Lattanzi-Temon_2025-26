package it.unicam.cs.ids.dto;

import java.time.LocalDate;

public record RegistraUtenteRequest(
        String nome,
        String cognome,
        String email,
        String nickname,
        String biografia,
        LocalDate dataDiNascita,
        boolean membroDiStaff
) {}