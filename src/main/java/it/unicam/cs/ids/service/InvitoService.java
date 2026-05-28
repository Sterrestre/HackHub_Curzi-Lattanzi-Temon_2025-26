package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.inviti.Invito;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class InvitoService {

    private final List<Invito> inviti = new ArrayList<>();

    public void salva(Invito invito) {
        inviti.add(invito);
    }

    public void elimina(Invito invito) {
        inviti.remove(invito);
    }

    public List<Invito> getInviti() {
        return Collections.unmodifiableList(inviti);
    }
}
