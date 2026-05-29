package it.unicam.cs.ids.handler;

public class Scaduto extends RuntimeException {
    public Scaduto() {
        super("La scadenza è già stata raggiunta");
    }
}
