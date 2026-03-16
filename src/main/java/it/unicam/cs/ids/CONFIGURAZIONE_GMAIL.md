# Configurazione Gmail API

1. Ottenere il file credentials.json dalle responsabile del progetto.
2. Salvare il file in una cartella locale (es: C:\segreti\gmail\credentials.json).
3. Impostare la variabile d'ambiente:
   Windows: setx GMAIL_CREDENTIALS_PATH "C:\segreti\gmail\credentials.json"
   Linux/macOS: export GMAIL_CREDENTIALS_PATH=/home/user/segreti/gmail/credentials.json
4. Avviare l'applicazione.