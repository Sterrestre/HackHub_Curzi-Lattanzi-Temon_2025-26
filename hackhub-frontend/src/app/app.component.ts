import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment';
import { UtenteDTO, UtenteService } from './services/utente.service';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, RouterLink, CommonModule],
    templateUrl: './app.component.html'
})
export class AppComponent implements OnInit {
    title = 'HackHub';

    // URL del backend per il login: e' un semplice link, non una chiamata
    // HttpClient, perche' il login OAuth2 di Spring Security funziona
    // tramite redirect del browser, non tramite fetch/XHR.
    loginUrl = `${environment.apiUrl}/oauth2/authorization/google`;

    utenteCorrente: UtenteDTO | null = null;
    caricamentoUtente = true;

    constructor(private utenteService: UtenteService) {}

    ngOnInit(): void {
        this.utenteService.getCorrente().subscribe({
            next: (utente) => {
                this.utenteCorrente = utente;
                this.caricamentoUtente = false;
            },
            error: () => {
                // 401: nessuno e' loggato, e' una situazione normale, non un errore da mostrare
                this.utenteCorrente = null;
                this.caricamentoUtente = false;
            }
        });
    }

    logout(): void {
        this.utenteService.logout().subscribe({
            next: () => {
                this.utenteCorrente = null;
                // Ricarica la pagina corrente per azzerare qualsiasi dato
                // eventualmente legato all'utente ora disconnesso.
                window.location.href = '/';
            },
            error: () => {
                // Anche in caso di errore, meglio azzerare lo stato lato client
                this.utenteCorrente = null;
            }
        });
    }
}