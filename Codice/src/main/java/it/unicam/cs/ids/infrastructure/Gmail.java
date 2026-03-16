package it.unicam.cs.ids.infrastructure;

/**
 * Interfaccia usata per mandare inviti attraverso un sistema esterno di mail, in particolare Gmail.
 */
public interface Gmail {
    void inviaEmail(String destinatario, String oggetto, String corpo);

}
