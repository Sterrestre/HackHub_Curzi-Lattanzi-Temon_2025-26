package it.unicam.cs.ids.model;

public class InvitoTeam extends Invito{
    private final MembroTeam mittente;

    public InvitoTeam(MembroTeam mittente, Utente destinatario) {
        super(destinatario);
        this.mittente = mittente;
    }


    public MembroTeam getMittente() {
        return mittente;
    }
}
