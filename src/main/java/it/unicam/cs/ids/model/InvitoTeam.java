package it.unicam.cs.ids.model;

public class InvitoTeam extends Invito{
    private final MembroTeam daParteDi;

    public InvitoTeam(MembroTeam daParteDi, Utente destinatario) {
        super(destinatario);
        this.daParteDi = daParteDi;
    }


    public MembroTeam getMittente() {
        return daParteDi;
    }
}
