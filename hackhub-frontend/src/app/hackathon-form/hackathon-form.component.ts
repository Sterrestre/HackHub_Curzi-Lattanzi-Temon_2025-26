import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { HackathonService } from '../services/hackathon.service';

@Component({
    selector: 'app-hackathon-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './hackathon-form.component.html'
})
export class HackathonFormComponent {
    inviato = false;
    errore: string | null = null;
    form: FormGroup;

    constructor(
        private fb: FormBuilder,
        private hackathonService: HackathonService,
        private router: Router
    ) {
        // NOTA: il campo "organizzatoreId" e' temporaneo. Quando sara' pronto
        // il login, questo valore verra' preso automaticamente dall'utente
        // autenticato (tramite il token JWT) invece di essere digitato a mano,
        // e questo campo del form verra' rimosso.
        this.form = this.fb.group({
            organizzatoreId: ['', Validators.required],
            nome: ['', Validators.required],
            regolamento: ['', Validators.required],
            dataInizio: ['', Validators.required],
            dataFine: ['', Validators.required],
            scadenzaIscrizioni: ['', Validators.required],
            luogo: ['', Validators.required],
            quotaIscrizione: [0, [Validators.required, Validators.min(0)]],
            premio: [0, [Validators.required, Validators.min(0)]],
            numMaxTeam: [1, [Validators.required, Validators.min(1)]],
            maxPartecipantiPerTeam: [1, [Validators.required, Validators.min(1)]]
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
            organizzatoreId: valori.organizzatoreId!,
            nome: valori.nome!,
            regolamento: valori.regolamento!,
            // Il backend si aspetta un LocalDateTime: aggiungiamo l'orario
            // se l'utente ha selezionato solo la data.
            dataInizio: this.toIsoDateTime(valori.dataInizio!),
            dataFine: this.toIsoDateTime(valori.dataFine!),
            scadenzaIscrizioni: this.toIsoDateTime(valori.scadenzaIscrizioni!),
            luogo: valori.luogo!,
            quotaIscrizione: valori.quotaIscrizione!,
            premio: valori.premio!,
            numMaxTeam: valori.numMaxTeam!,
            maxPartecipantiPerTeam: valori.maxPartecipantiPerTeam!
        }).subscribe({
            next: () => {
                this.inviato = true;
                setTimeout(() => this.router.navigate(['/']), 1500);
            },
            error: (err) => {
                this.errore = err?.error ?? 'Errore durante la creazione dell\'hackathon.';
            }
        });
    }

    private toIsoDateTime(valoreInput: string): string {
        // Un <input type="datetime-local"> restituisce es. "2026-08-10T15:00";
        // aggiungiamo i secondi per un formato ISO completo.
        return valoreInput.length === 16 ? `${valoreInput}:00` : valoreInput;
    }
}