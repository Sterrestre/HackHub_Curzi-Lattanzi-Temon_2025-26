package it.unicam.cs.ids.dto;

import java.time.LocalDateTime;

public record ProponiCallRequest(
        String richiestaId,
        LocalDateTime dataOra
) {}

