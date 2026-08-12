import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { InvitoStaff, InvitoService } from '../services/invito.service';

@Component({
    selector: 'app-miei-inviti',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './miei-inviti.component.html'
})
export class MieiInvitiComponent implements OnInit {
    inviti: InvitoStaff[] = [];
    caricamento = true;
    errore: string | null = null;
    messaggio: string | null = null;

    constructor(private invitoService: InvitoService) {}

    ngOnInit(): void {
        this.carica();
    }

    carica(): void {
        this.caricamento = true;
        this.invitoService.getMieiInviti().subscribe({
            next: (dati) => {
                this.inviti = dati;
                this.caricamento = false;
            },
            error: () => {
                this.errore = 'Impossibile caricare gli inviti. Assicurati di aver effettuato il login.';
                this.caricamento = false;
            }
        });
    }

    rispondi(invito: InvitoStaff, accetta: boolean): void {
        this.invitoService.rispondi(invito.id, accetta).subscribe({
            next: () => {
                this.messaggio = accetta ? 'Invito accettato!' : 'Invito rifiutato.';
                this.carica();
            },
            error: (err) => {
                this.errore = err?.error ?? 'Errore durante la risposta all\'invito.';
            }
        });
    }
}