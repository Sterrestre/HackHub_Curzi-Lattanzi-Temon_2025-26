package it.unicam.cs.ids.dto;

public record AssegnaMentoreRequest(
        String hackathonId,
        String teamId,
        String ruoloMentoreId
) {}