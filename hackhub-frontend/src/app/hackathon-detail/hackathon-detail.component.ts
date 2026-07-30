import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Hackathon, HackathonService } from '../services/hackathon.service';
import { Sottomissione, SottomissioneService } from '../services/sottomissione.service';

@Component({
    selector: 'app-hackathon-detail',
    standalone: true,
    imports: [CommonModule, RouterLink],
    templateUrl: './hackathon-detail.component.html'
})
export class HackathonDetailComponent implements OnInit {
    hackathon: Hackathon | null = null;
    classifica: string[] = [];
    sottomissioni: Sottomissione[] = [];
    caricamento = true;
    errore = false;

    constructor(
        private route: ActivatedRoute,
        private hackathonService: HackathonService,
        private sottomissioneService: SottomissioneService
    ) {}

    ngOnInit(): void {
        // L'id fa parte dell'URL (definito nella rotta 'hackathon/:id'),
        // lo leggiamo tramite ActivatedRoute.
        const id = this.route.snapshot.paramMap.get('id');
        if (!id) {
            this.errore = true;
            this.caricamento = false;
            return;
        }

        this.hackathonService.getById(id).subscribe({
            next: (dati) => {
                this.hackathon = dati;
                this.caricamento = false;
            },
            error: () => {
                this.errore = true;
                this.caricamento = false;
            }
        });

        this.hackathonService.getClassifica(id).subscribe({
            next: (dati) => (this.classifica = dati),
            error: () => (this.classifica = [])
        });

        this.sottomissioneService.getByHackathon(id).subscribe({
            next: (dati) => (this.sottomissioni = dati),
            error: () => (this.sottomissioni = [])
        });
    }
}