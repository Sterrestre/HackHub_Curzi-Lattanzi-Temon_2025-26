import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Hackathon, HackathonService } from '../services/hackathon.service';
import { Sottomissione, SottomissioneService } from '../services/sottomissione.service';

interface ValutazioneInput {
    voto: number;
    giudizio: string;
}

@Component({
    selector: 'app-hackathon-detail',
    standalone: true,
    imports: [CommonModule, RouterLink, FormsModule],
    templateUrl: './hackathon-detail.component.html'
})
export class HackathonDetailComponent implements OnInit {
    hackathonId = '';
    hackathon: Hackathon | null = null;
    classifica: string[] = [];
    sottomissioni: Sottomissione[] = [];
    caricamento = true;
    errore = false;

    confermaInCorso = false;
    confermaErrore: string | null = null;

    // Un piccolo form di valutazione per ciascuna sottomissione non ancora
    // valutata, indicizzato per teamIscrittoId. L'id del giudice non serve
    // piu': arriva automaticamente dalla sessione dell'utente loggato.
    valutazioneInput: { [teamIscrittoId: string]: ValutazioneInput } = {};
    valutazioneErrore: { [teamIscrittoId: string]: string } = {};

    constructor(
        private route: ActivatedRoute,
        private hackathonService: HackathonService,
        private sottomissioneService: SottomissioneService
    ) {}

    ngOnInit(): void {
        const id = this.route.snapshot.paramMap.get('id');
        if (!id) {
            this.errore = true;
            this.caricamento = false;
            return;
        }
        this.hackathonId = id;
        this.caricaTutto();
    }

    caricaTutto(): void {
        this.caricamento = true;

        this.hackathonService.getById(this.hackathonId).subscribe({
            next: (dati) => {
                this.hackathon = dati;
                this.caricamento = false;
            },
            error: () => {
                this.errore = true;
                this.caricamento = false;
            }
        });

        this.hackathonService.getClassifica(this.hackathonId).subscribe({
            next: (dati) => (this.classifica = dati),
            error: () => (this.classifica = [])
        });

        this.sottomissioneService.getByHackathon(this.hackathonId).subscribe({
            next: (dati) => {
                this.sottomissioni = dati;
                // Inizializza il form di valutazione per ogni sottomissione non valutata
                for (const s of dati) {
                    if (!s.valutata && !this.valutazioneInput[s.teamIscrittoId]) {
                        this.valutazioneInput[s.teamIscrittoId] = { voto: 0, giudizio: '' };
                    }
                }
            },
            // Se la chiamata fallisce (es. 403 perche' non sei staff di questo
            // hackathon), mostriamo semplicemente nessuna sottomissione invece
            // di un errore a schermo.
            error: () => (this.sottomissioni = [])
        });
    }

    confermaHackathon(): void {
        this.confermaInCorso = true;
        this.confermaErrore = null;

        this.hackathonService.conferma(this.hackathonId).subscribe({
            next: () => {
                this.confermaInCorso = false;
                this.caricaTutto();
            },
            error: (err) => {
                this.confermaInCorso = false;
                this.confermaErrore = err?.error ?? 'Errore durante la conferma dell\'hackathon.';
            }
        });
    }

    valuta(s: Sottomissione): void {
        const input = this.valutazioneInput[s.teamIscrittoId];
        if (!input || !input.giudizio) {
            this.valutazioneErrore[s.teamIscrittoId] = 'Compila voto e giudizio.';
            return;
        }

        this.valutazioneErrore[s.teamIscrittoId] = '';

        this.sottomissioneService.valuta({
            hackathonId: this.hackathonId,
            teamIscrittoId: s.teamIscrittoId,
            voto: input.voto,
            giudizio: input.giudizio
        }).subscribe({
            next: () => this.caricaTutto(),
            error: (err) => {
                this.valutazioneErrore[s.teamIscrittoId] = err?.error ?? 'Errore durante la valutazione.';
            }
        });
    }
}