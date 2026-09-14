package it.unicam.cs.ids.service.infrastructure.gmail;

import java.io.File;
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

        public static InputStream getCredentialsStream() throws IOException {
            String envPath = System.getenv("GMAIL_CREDENTIALS_PATH");

            // Se la variabile d'ambiente non c'è, usa il percorso relativo di default
            String path = (envPath != null && !envPath.isBlank()) ? envPath : "./secrets/credentials.json";

            File file = new File(path);
            if (!file.exists()) {
                throw new IllegalStateException(
                        "File credenziali Gmail non trovato! Cercato in: " + file.getAbsolutePath()
                );
            }

            return new FileInputStream(file);
        }
}
