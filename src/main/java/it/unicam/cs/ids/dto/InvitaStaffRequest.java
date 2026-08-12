package it.unicam.cs.ids.dto;

import it.unicam.cs.ids.model.staff.RuoliStaff;

public record InvitaStaffRequest(
        String hackathonId,
        String email,
        RuoliStaff ruolo
) {}