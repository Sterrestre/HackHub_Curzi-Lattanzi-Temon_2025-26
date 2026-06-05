package it.unicam.cs.ids.dto;

public record InvitaRequest(
        String teamId,
        String adminId,
        String utenteId
) {}

