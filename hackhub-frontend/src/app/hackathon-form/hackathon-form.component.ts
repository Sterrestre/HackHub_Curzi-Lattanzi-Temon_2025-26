import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HackathonService } from '../services/hackathon.service';
import { InvitoService } from '../services/invito.service';
import { estraiMessaggioErrore } from '../utils/errore.util';

@Component({
    selector: 'app-hackathon-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './hackathon-form.component.html'
})
export class HackathonFormComponent {
    inviato = false;
    errore: string | null = null;
    avvisoInviti: string | null = null;
    form: FormGroup;

    constructor(
        private fb: FormBuilder,
        private hackathonService: HackathonService,
        private invitoService: InvitoService,
        private router: Router
    ) {
        this.form = this.fb.group({
            nome: ['', Validators.required],
            regolamento: ['', Validators.required],
            dataInizio: ['', Validators.required],
            dataFine: ['', Validators.required],
            scadenzaIscrizioni: ['', Validators.required],
            luogo: ['', Validators.required],
            quotaIscrizione: [0, [Validators.required, Validators.min(0)]],
            premio: [0, [Validators.required, Validators.min(0)]],
            numMaxTeam: [1, [Validators.required, Validators.min(1)]],
            maxPartecipantiPerTeam: [1, [Validators.required, Validators.min(1)]],
            // Facoltativi: se compilati, dopo la creazione l'organizzatore
            // invita subito queste persone come giudice/mentore.
            emailGiudice: [''],
            emailMentore: ['']
        });
    }

    onSubmit(): void {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.errore = null;
        const valori = this.form.getRawValue();

        this.hackathonService.crea({
            nome: valori.nome!,
            regolamento: valori.regolamento!,
            dataInizio: this.toIsoDateTime(valori.dataInizio!),
            dataFine: this.toIsoDateTime(valori.dataFine!),
            scadenzaIscrizioni: this.toIsoDateTime(valori.scadenzaIscrizioni!),
            luogo: valori.luogo!,
            quotaIscrizione: valori.quotaIscrizione!,
            premio: valori.premio!,
            numMaxTeam: valori.numMaxTeam!,
            maxPartecipantiPerTeam: valori.maxPartecipantiPerTeam!
        }).subscribe({
            next: (hackathonCreato: any) => {
                this.inviato = true;
                this.inviaInvitiOpzionali(hackathonCreato.id, valori.emailGiudice, valori.emailMentore);
            },
            error: (err) => {
                this.errore = estraiMessaggioErrore(err, 'Errore durante la creazione dell\'hackathon.');
            }
        });
    }

    private inviaInvitiOpzionali(hackathonId: string, emailGiudice: string, emailMentore: string): void {
        const avvisi: string[] = [];

        if (emailGiudice) {
            this.invitoService.invitaStaff({ hackathonId, email: emailGiudice, ruolo: 'GIUDICE' }).subscribe({
                error: (err) => avvisi.push(`Giudice non invitato: ${err?.error ?? 'errore sconosciuto'}`)
            });
        }
        if (emailMentore) {
            this.invitoService.invitaStaff({ hackathonId, email: emailMentore, ruolo: 'MENTORE' }).subscribe({
                error: (err) => avvisi.push(`Mentore non invitato: ${err?.error ?? 'errore sconosciuto'}`)
            });
        }

        // Diamo qualche istante alle due chiamate (indipendenti) di completarsi
        // prima di mostrare eventuali avvisi e tornare alla lista.
        setTimeout(() => {
            if (avvisi.length > 0) {
                this.avvisoInviti = avvisi.join(' — ') +
                    ' (l\'hackathon è stato comunque creato correttamente; puoi invitare di nuovo dal suo dettaglio)';
            } else {
                this.router.navigate(['/']);
            }
        }, 1200);
    }

    private toIsoDateTime(valoreInput: string): string {
        return valoreInput.length === 16 ? `${valoreInput}:00` : valoreInput;
    }
}