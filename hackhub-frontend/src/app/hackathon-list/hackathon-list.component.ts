import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Hackathon, HackathonService } from '../services/hackathon.service';

@Component({
    selector: 'app-hackathon-list',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './hackathon-list.component.html'
})
export class HackathonListComponent implements OnInit {
    hackathons: Hackathon[] = [];
    caricamento = true;
    errore = false;

    constructor(private hackathonService: HackathonService) {}

    ngOnInit(): void {
        this.hackathonService.getAll().subscribe({
            next: (dati) => {
                this.hackathons = dati;
                this.caricamento = false;
            },
            error: () => {
                this.errore = true;
                this.caricamento = false;
            }
        });
    }
}