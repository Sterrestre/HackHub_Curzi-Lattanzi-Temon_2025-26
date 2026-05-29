package it.unicam.cs.ids.model.team;

import it.unicam.cs.ids.model.Utente;
import it.unicam.cs.ids.model.team.MembroTeam;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Team {
    private String teamID;
    private String nome;
    private List<MembroTeam> membriTeam = new ArrayList<>();

    /**
     * Crea un team, associandogli il nome corrispondente e un ID univoco.
     * @param nome il nome del team.
     * @throws IllegalArgumentException se il nome è null o vuoto
     */
    public Team(String nome) {
        this.teamID = UUID.randomUUID().toString();
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome del team non può essere vuoto");
        }
        this.nome = nome;
    }

    /**
     * Permette di recuperare l'ID univoco del team
     * @return l'ID del team.
     */
    public String getTeamID() {
        return teamID;
    }

    /**
     * Restituisce il nome del team.
     * @return il nome del team.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Permette di cambiare il nome del team.
     * @param nome il nuovo nome del team.
     * @throws IllegalArgumentException se il nome è null o vuoto
     */
    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Il nome del team non può essere vuoto");
        }
        this.nome = nome;
    }

    public List<MembroTeam> getMembri() {
        return membriTeam;
    }

    public MembroTeam aggiungiMembro(Utente utente) {
        // evita duplicati
        if (membriTeam.stream().anyMatch(m -> m.getUtente().equals(utente))) {
            throw new IllegalArgumentException("L'utente è già membro del team");
        }

        MembroTeam membro = new MembroTeam(utente, this);

        // Aggiornamento della relazione bidirezionale
        membriTeam.add(membro);
        utente.setTeam(this);

        return membro;
    }

    public void rimuoviMembro(MembroTeam membro) {
        if (!membriTeam.contains(membro)) {
            throw new IllegalArgumentException("Il membro non appartiene al team");
        }

        membro.getUtente().setTeam(null);
        membriTeam.remove(membro);

        // Invariante: un team deve avere almeno un amministratore
        if (membriTeam.stream().noneMatch(MembroTeam::isAmministratore)) {
            throw new IllegalStateException("Il team non può rimanere senza amministratori");
        }
    }


    public void rendiAmministratore(MembroTeam membro) {
        if (!membriTeam.contains(membro)) {
            throw new IllegalArgumentException("L'utente non appartiene al team");
        }
        membro.setAmministratore(true);
    }

    public boolean isAmministratore(Utente utente) {
        return membriTeam.stream()
                .anyMatch(m -> m.getUtente().equals(utente) && m.isAmministratore());
    }

    public List<MembroTeam> getAmministratori() {
        return membriTeam.stream()
                .filter(MembroTeam::isAmministratore)
                .collect(Collectors.toUnmodifiableList());
    }
}
