import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { TeamService } from '../services/team.service';
import { UtenteService } from '../services/utente.service';
import { estraiMessaggioErrore } from '../utils/errore.util';

@Component({
    selector: 'app-team-create',
    standalone: true,
    imports: [CommonModule, ReactiveFormsModule, RouterLink],
    templateUrl: './team-create.component.html'
})
export class TeamCreateComponent implements OnInit {
    form: FormGroup;
    caricamentoUtente = true;
    teamEsistenteId: string | null = null;
    errore: string | null = null;
    inCorso = false;

    constructor(
        private fb: FormBuilder,
        private teamService: TeamService,
        private utenteService: UtenteService,
        private router: Router
    ) {
        this.form = this.fb.group({
            nome: ['', Validators.required]
        });
    }

    ngOnInit(): void {
        this.utenteService.getCorrente().subscribe({
            next: (utente) => {
                this.teamEsistenteId = utente.teamId;
                this.caricamentoUtente = false;
            },
            error: () => {
                this.caricamentoUtente = false;
            }
        });
    }

    onSubmit(): void {
        if (this.form.invalid) {
            this.form.markAllAsTouched();
            return;
        }

        this.inCorso = true;
        this.errore = null;

        this.teamService.crea({
            nome: this.form.value.nome!
        }).subscribe({
            next: (team: any) => {
                this.inCorso = false;
                this.router.navigate(['/team', team.id]);
            },
            error: (err) => {
                this.inCorso = false;
                this.errore = estraiMessaggioErrore(err, 'Errore durante la creazione del team.');
            }
        });
    }
}