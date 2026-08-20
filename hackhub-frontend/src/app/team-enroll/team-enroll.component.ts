import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { TeamService } from '../services/team.service';
import { UtenteService } from '../services/utente.service';
import { estraiMessaggioErrore } from '../utils/errore.util';

@Component({
    selector: 'app-team-enroll',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './team-enroll.component.html'
})
export class TeamEnrollComponent implements OnInit {
    hackathonId = '';
    inviato = false;
    errore: string | null = null;

    caricamentoUtente = true;
    teamEsistenteId: string | null = null;

    teamIscrittoId: string | null = null;

    constructor(
        private route: ActivatedRoute,
        private teamService: TeamService,
        private utenteService: UtenteService
    ) {}

    ngOnInit(): void {
        this.hackathonId = this.route.snapshot.paramMap.get('id') ?? '';

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

    iscriviTeam(): void {
        if (!this.teamEsistenteId) return;

        this.errore = null;
        this.teamService.iscriviAHackathon(this.hackathonId, {
            teamId: this.teamEsistenteId
        }).subscribe({
            next: (iscrizione) => {
                this.teamIscrittoId = iscrizione.teamIscrittoId;
                this.inviato = true;
            },
            error: (err) => {
                this.errore = estraiMessaggioErrore(err, 'Errore durante l\'iscrizione del team.');
            }
        });
    }
}