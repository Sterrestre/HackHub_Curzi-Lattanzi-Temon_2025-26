package it.unicam.cs.ids.service;

import it.unicam.cs.ids.model.inviti.Invito;
import it.unicam.cs.ids.repository.InvitoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvitoService {

    private final InvitoRepository invitoRepository;

    public InvitoService(InvitoRepository invitoRepository) {
        this.invitoRepository = invitoRepository;
    }

    public Invito salva(Invito invito) {
        return invitoRepository.save(invito);
    }

    public void elimina(Invito invito) {
        invitoRepository.delete(invito);
    }

    public Invito findById(String id) {
        return invitoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invito non trovato"));
    }

    public List<Invito> findAll() {
        return invitoRepository.findAll();
    }
}

