package it.unicam.cs.ids.controller;

import it.unicam.cs.ids.model.team.MembroTeam;
import it.unicam.cs.ids.service.team.MembriTeamService;
import org.springframework.stereotype.Service;

@Service
public class MembriTeamHandler {

    private final MembriTeamService membriTeamService;

    public MembriTeamHandler(MembriTeamService membriTeamService) {
        this.membriTeamService = membriTeamService;
    }

    public void lasciaTeam(MembroTeam membroTeam) {
        membriTeamService.lasciaTeam(membroTeam);
    }
}
