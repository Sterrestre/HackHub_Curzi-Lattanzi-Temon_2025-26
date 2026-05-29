package it.unicam.cs.ids.handler;

public class UnicoTeamException extends RuntimeException {
    public UnicoTeamException() {
        super("L'utente fa già parte di un team");
    }
}
