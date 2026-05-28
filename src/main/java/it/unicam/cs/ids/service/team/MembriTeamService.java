package it.unicam.cs.ids.service.team;

import it.unicam.cs.ids.model.team.MembroTeam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MembriTeamService {

    private final List<MembroTeam> membriTeam = new ArrayList<>();

    public void salva(MembroTeam  membroTeam) {
        membriTeam.add(membroTeam);
    }

    public void lasciaTeam(MembroTeam  membroTeam) {
        membriTeam.remove(membroTeam);
    }

    public List<MembroTeam> getMemebriTeam() {
        return Collections.unmodifiableList(membriTeam);
    }
}
