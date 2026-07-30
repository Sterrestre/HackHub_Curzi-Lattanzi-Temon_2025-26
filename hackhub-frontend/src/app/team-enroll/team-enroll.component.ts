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

    constructor(
        private fb: FormBuilder,
        private route: ActivatedRoute,
        private teamService: TeamService,
        private router: Router
    ) {
        // NOTA: "amministratoreId" e "teamId" sono temporanei: dopo il login
        // l'utente non dovra' piu' inserirli a mano (l'id utente arrivera' dal
        // token, e il team lo si scegliera' da un elenco dei propri team invece
        // che digitarne l'id).
        this.form = this.fb.group({
            nomeTeam: ['', Validators.required],
            amministratoreId: ['', Validators.required]
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
            amministratoreId: valori.amministratoreId!
        }).subscribe({
            next: (teamCreato: any) => {
                this.teamService.iscriviAHackathon(this.hackathonId, {
                    teamId: teamCreato.id,
                    amministratoreId: valori.amministratoreId!
                }).subscribe({
                    next: () => {
                        this.inviato = true;
                        setTimeout(() => this.router.navigate(['/hackathon', this.hackathonId]), 1500);
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