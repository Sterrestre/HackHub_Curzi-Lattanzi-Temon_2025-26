package it.unicam.cs.ids.model;

import it.unicam.cs.ids.Invito;

/**
 * Interfaccia che si occupa dell'invio di un {@code Invito} a un destinatario.
 * Le implementazioni concreti possono inviare l'invito via email. Il metodo {@code invia}
 * riceve l'oggetto {@code Invito} contenente tutte le informazioni necessarie.
 */
public interface InvitoSender {

    /**
     * Invia l'invito specificato.
     *
     * @param invito l'oggetto {@code Invito} da inviare; non deve essere {@code null}
     */
    void invia(Invito invito);
}