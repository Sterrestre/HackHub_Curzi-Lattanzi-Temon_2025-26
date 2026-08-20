import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { Hackathon, HackathonService } from '../services/hackathon.service';
import { Sottomissione, SottomissioneService } from '../services/sottomissione.service';
import { InvitoService } from '../services/invito.service';
import { UtenteService } from '../services/utente.service';
import { estraiMessaggioErrore } from '../utils/errore.util';

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

    sonOrganizzatore = false;
    sonoStaff = false;
    mostraIscriviTeam = true;

    confermaInCorso = false;
    confermaErrore: string | null = null;

    classificaInCorso = false;
    classificaErrore: string | null = null;
    classificaMessaggio: string | null = null;

    invitoEmail = '';
    invitoRuolo: 'GIUDICE' | 'MENTORE' = 'GIUDICE';
    invitoInCorso = false;
    invitoErrore: string | null = null;
    invitoMessaggio: string | null = null;

    valutazioneInput: { [teamIscrittoId: string]: ValutazioneInput } = {};
    valutazioneErrore: { [teamIscrittoId: string]: string } = {};

    constructor(
        private route: ActivatedRoute,
        private hackathonService: HackathonService,
        private sottomissioneService: SottomissioneService,
        private invitoService: InvitoService,
        private utenteService: UtenteService
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
                this.determinaVisibilita();
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
                for (const s of dati) {
                    if (!s.valutata && !this.valutazioneInput[s.teamIscrittoId]) {
                        this.valutazioneInput[s.teamIscrittoId] = { voto: 0, giudizio: '' };
                    }
                }
            },
            error: () => (this.sottomissioni = [])
        });
    }

    private determinaVisibilita(): void {
        if (!this.hackathon) return;

        this.utenteService.getCorrente().subscribe({
            next: (utente) => {
                this.sonOrganizzatore = utente.id === this.hackathon!.organizzatoreId;

                this.hackathonService.sonoStaff(this.hackathonId).subscribe({
                    next: (staff) => {
                        this.sonoStaff = staff;
                        this.mostraIscriviTeam = !this.sonOrganizzatore && !this.sonoStaff;
                    },
                    error: () => {
                        this.sonoStaff = false;
                        this.mostraIscriviTeam = !this.sonOrganizzatore;
                    }
                });
            },
            error: () => {
                this.sonOrganizzatore = false;
                this.sonoStaff = false;
                this.mostraIscriviTeam = true;
            }
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
                this.confermaErrore = estraiMessaggioErrore(err, 'Errore durante la conferma dell\'hackathon.');
            }
        });
    }

    pubblicaClassifica(): void {
        this.classificaInCorso = true;
        this.classificaErrore = null;
        this.classificaMessaggio = null;

        this.hackathonService.confermaClassifica(this.hackathonId).subscribe({
            next: () => {
                this.classificaInCorso = false;
                this.classificaMessaggio = 'Classifica pubblicata!';
            },
            error: (err) => {
                this.classificaInCorso = false;
                this.classificaErrore = estraiMessaggioErrore(err, 'Errore durante la pubblicazione della classifica.');
            }
        });
    }

    invitaStaff(): void {
        if (!this.invitoEmail) {
            this.invitoErrore = 'Inserisci l\'email dell\'utente da invitare.';
            return;
        }

        this.invitoInCorso = true;
        this.invitoErrore = null;
        this.invitoMessaggio = null;

        this.invitoService.invitaStaff({
            hackathonId: this.hackathonId,
            email: this.invitoEmail,
            ruolo: this.invitoRuolo
        }).subscribe({
            next: () => {
                this.invitoInCorso = false;
                this.invitoMessaggio = 'Invito inviato!';
                this.invitoEmail = '';
            },
            error: (err) => {
                this.invitoInCorso = false;
                this.invitoErrore = estraiMessaggioErrore(err, 'Errore durante l\'invio dell\'invito.');
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
                this.valutazioneErrore[s.teamIscrittoId] = estraiMessaggioErrore(err, 'Errore durante la valutazione.');
            }
        });
    }
}