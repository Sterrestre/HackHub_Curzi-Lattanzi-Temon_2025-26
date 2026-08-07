import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { SottomissioneService } from '../services/sottomissione.service';

@Component({
    selector: 'app-sottomissione-form',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './sottomissione-form.component.html'
})
export class SottomissioneFormComponent implements OnInit {
    hackathonId = '';
    inviato = false;
    errore: string | null = null;
    form: FormGroup;

    constructor(
        private fb: FormBuilder,
        private route: ActivatedRoute,
        private sottomissioneService: SottomissioneService
    ) {
        // NOTA: "teamIscrittoId" e' temporaneo (lo trovi nella pagina di
        // conferma dopo l'iscrizione del team). Dopo il login verra' dedotto
        // automaticamente dal team dell'utente autenticato.
        this.form = this.fb.group({
            teamIscrittoId: ['', Validators.required],
            titolo: ['', Validators.required],
            descrizione: ['', Validators.required],
            linkRepository: ['', Validators.required]
        });
    }

    ngOnInit(): void {
        this.hackathonId = this.route.snapshot.paramMap.get('id') ?? '';
    }

    onSubmit(): void {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.errore = null;
        const valori = this.form.getRawValue();

        this.sottomissioneService.carica({
            teamIscrittoId: valori.teamIscrittoId!,
            hackathonId: this.hackathonId,
            titolo: valori.titolo!,
            descrizione: valori.descrizione!,
            linkRepository: valori.linkRepository!
        }).subscribe({
            next: () => {
                this.inviato = true;
            },
            error: (err) => {
                this.errore = err?.error ?? 'Errore durante il caricamento della sottomissione.';
            }
        });
    }
}