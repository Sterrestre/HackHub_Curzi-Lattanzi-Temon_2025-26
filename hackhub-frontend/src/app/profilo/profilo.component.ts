import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { UtenteDTO, UtenteService } from '../services/utente.service';
import { Hackathon, HackathonService } from '../services/hackathon.service';

@Component({
    selector: 'app-profilo',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './profilo.component.html'
})
export class ProfiloComponent implements OnInit {
    utente: UtenteDTO | null = null;
    hackathonComeTeam: Hackathon[] = [];
    hackathonComeStaff: Hackathon[] = [];
    caricamento = true;
    errore = false;

    constructor(
        private utenteService: UtenteService,
        private hackathonService: HackathonService
    ) {}

    ngOnInit(): void {
        this.utenteService.getCorrente().subscribe({
            next: (dati) => {
                this.utente = dati;
                this.caricamento = false;
            },
            error: () => {
                this.errore = true;
                this.caricamento = false;
            }
        });

        this.hackathonService.getMieiComeTeam().subscribe({
            next: (dati) => (this.hackathonComeTeam = dati),
            error: () => (this.hackathonComeTeam = [])
        });

        this.hackathonService.getMieiComeStaff().subscribe({
            next: (dati) => (this.hackathonComeStaff = dati),
            error: () => (this.hackathonComeStaff = [])
        });
    }
}