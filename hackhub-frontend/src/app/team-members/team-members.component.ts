import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { MembroTeam, TeamService } from '../services/team.service';
import { estraiMessaggioErrore } from '../utils/errore.util';

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
        this.form = this.fb.group({
            email: ['', [Validators.required, Validators.email]]
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

        this.teamService.invitaMembro({
            teamId: this.teamId,
            email: this.form.value.email!
        }).subscribe({
            next: () => {
                this.inviato = true;
                this.form.reset();
            },
            error: (err) => {
                this.errore = estraiMessaggioErrore(err, 'Errore durante l\'invio dell\'invito.');
            }
        });
    }
}