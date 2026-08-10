import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { TeamService } from '../services/team.service';

@Component({
    selector: 'app-team-enroll',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './team-enroll.component.html'
})
export class TeamEnrollComponent implements OnInit {
    hackathonId = '';
    inviato = false;
    errore: string | null = null;
    form: FormGroup;

    // Popolati dopo l'iscrizione, per mostrare all'utente gli id da usare
    // nei prossimi passi (aggiungere membri, caricare la sottomissione)
    // finche' non c'e' il login a gestirli automaticamente.
    teamCreatoId: string | null = null;
    teamIscrittoId: string | null = null;

    constructor(
        private fb: FormBuilder,
        private route: ActivatedRoute,
        private teamService: TeamService,
        private router: Router
    ) {
        // NOTA: "amministratoreId" e' temporaneo: dopo il login l'utente
        // non dovra' piu' inserirlo a mano, arrivera' dal token.
        this.form = this.fb.group({
            nomeTeam: ['', Validators.required],
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

        // Passo 1: crea il team. Passo 2: iscrivilo a questo hackathon.
        this.teamService.crea({
            nome: valori.nomeTeam!,
        }).subscribe({
            next: (teamCreato: any) => {
                this.teamCreatoId = teamCreato.id;

                this.teamService.iscriviAHackathon(this.hackathonId, {
                    teamId: teamCreato.id,
                }).subscribe({
                    next: (iscrizione) => {
                        this.teamIscrittoId = iscrizione.teamIscrittoId;
                        this.inviato = true;
                    },
                    error: (err) => {
                        this.errore = err?.error ?? 'Errore durante l\'iscrizione del team.';
                    }
                });
            },
            error: (err) => {
                this.errore = err?.error ?? 'Errore durante la creazione del team.';
            }
        });
    }
}