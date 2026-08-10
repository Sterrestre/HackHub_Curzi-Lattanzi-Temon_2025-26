import { HttpInterceptorFn } from '@angular/common/http';

/**
 * Allega automaticamente i cookie (compreso quello di sessione OAuth2)
 * a ogni richiesta HTTP verso il backend. Necessario perche' il login
 * e' basato su sessione, non su token: senza questo, il backend non
 * riconoscerebbe l'utente come autenticato nelle richieste successive
 * al login, specialmente quando frontend e backend sono su origini
 * diverse (es. in produzione, prima dell'unificazione tramite Nginx).
 */
export const credentialsInterceptor: HttpInterceptorFn = (req, next) => {
    const richiestaConCredenziali = req.clone({ withCredentials: true });
    return next(richiestaConCredenziali);
};