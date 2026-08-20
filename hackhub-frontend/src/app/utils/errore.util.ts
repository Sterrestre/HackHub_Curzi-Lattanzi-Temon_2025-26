/**
 * Estrae un messaggio d'errore leggibile da una risposta HTTP fallita.
 * Il backend a volte risponde con una stringa semplice (es. "Utente non
 * trovato"), altre volte con un oggetto JSON (es. per errori di validazione
 * non gestiti esplicitamente) - senza questa funzione, il secondo caso
 * verrebbe mostrato come "[object Object]" invece di un testo leggibile.
 */
export function estraiMessaggioErrore(err: any, messaggioDefault = 'Si è verificato un errore. Riprova.'): string {
    if (typeof err?.error === 'string') {
        return err.error;
    }
    if (err?.status === 401 || err?.status === 403) {
        return 'Non hai i permessi per completare questa azione (o devi effettuare il login).';
    }
    if (err?.error?.message) {
        return err.error.message;
    }
    return messaggioDefault;
}