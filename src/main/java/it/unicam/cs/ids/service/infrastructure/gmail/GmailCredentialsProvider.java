package it.unicam.cs.ids.service.infrastructure.gmail;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Fornisce accesso alle credenziali dell'API Gmail dal file system.
 *
 * Questa classe utility gestisce il recupero delle credenziali dell'API Gmail
 * leggendo da un percorso di file specificato nella variabile d'ambiente
 * GMAIL_CREDENTIALS_PATH. Il file delle credenziali è tipicamente un file JSON
 * contenente le informazioni di autenticazione OAuth2 richieste dal client
 * dell'API Gmail di Google.
 *
 */
public class GmailCredentialsProvider {

    /**
     * Recupera un flusso di input per il file delle credenziali dell'API Gmail.
     *
     * Questo metodo legge il percorso del file di credenziali di Gmail dalla
     * variabile d'ambiente GMAIL_CREDENTIALS_PATH e apre un flusso di input
     * verso quel file. Il file delle credenziali è atteso essere in formato JSON
     * come richiesto dall'API Gmail di Google.
     *
     * @return un InputStream contenente le credenziali dell'API Gmail
     * @throws IOException se si verifica un errore di I/O durante l'apertura
     *         del flusso del file
     * @throws IllegalStateException se la variabile d'ambiente GMAIL_CREDENTIALS_PATH
     *         non è impostata o è vuota
     *
     */
    public static InputStream getCredentialsStream() throws IOException {
        String path = System.getenv("GMAIL_CREDENTIALS_PATH");

        if (path == null || path.isBlank()) {
            throw new IllegalStateException("La variabile d'ambiente GMAIL_CREDENTIALS_PATH non è impostata.");
        }

        return new FileInputStream(path);
    }
}
