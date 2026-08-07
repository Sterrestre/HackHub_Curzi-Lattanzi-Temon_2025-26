import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MembroTeam, TeamService } from '../services/team.service';

@Component({
    selector: 'app-team-members',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './team-members.component.html'
})
export class TeamMembersComponent implements OnInit {
    teamId = '';
    membri: MembroTeam[] = [];
    caricamento = true;
    errore: string | null = null;
    inviato = false;
    form: FormGroup;

    constructor(
        private fb: FormBuilder,
        private route: ActivatedRoute,
        private teamService: TeamService
    ) {
        // NOTA: "utenteId" e' temporaneo, dopo il login si scegliera'
        // l'utente da un elenco (es. tramite invito) invece di digitarne l'id.
        this.form = this.fb.group({
            utenteId: ['', Validators.required],
            amministratore: [false]
        });
    }

    ngOnInit(): void {
        this.teamId = this.route.snapshot.paramMap.get('teamId') ?? '';
        this.caricaMembri();
    }

    caricaMembri(): void {
        this.caricamento = true;
        this.teamService.getMembri(this.teamId).subscribe({
            next: (dati) => {
                this.membri = dati;
                this.caricamento = false;
            },
            error: () => {
                this.errore = 'Impossibile caricare i membri del team.';
                this.caricamento = false;
            }
        });
    }

    onSubmit(): void {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.errore = null;
        const valori = this.form.getRawValue();

        this.teamService.aggiungiMembro({
            teamId: this.teamId,
            utenteId: valori.utenteId!,
            amministratore: valori.amministratore!
        }).subscribe({
            next: () => {
                this.inviato = true;
                this.form.reset({ amministratore: false });
                this.caricaMembri();
            },
            error: (err) => {
                this.errore = err?.error ?? 'Errore durante l\'aggiunta del membro.';
            }
        });
    }
}