import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface InfoHack {
    regolamento: string;
    dataInizio: string;
    dataFine: string;
    scadenzaIscrizioni: string;
    luogo: string;
    quotaIscrizione: number;
    premio: number;
    numMaxTeam: number;
    maxPartecipantiPerTeam: number;
}

export interface Hackathon {
    id: string;
    nome: string;
    info: InfoHack;
    numTeamIscritti: number;
    stato: string;
}

@Injectable({
    providedIn: 'root'
})
export class HackathonService {
    private readonly baseUrl = `${environment.apiUrl}/hackathon`;

    constructor(private http: HttpClient) {}

    getAll(): Observable<Hackathon[]> {
        return this.http.get<Hackathon[]>(`${this.baseUrl}/all`);
    }
}