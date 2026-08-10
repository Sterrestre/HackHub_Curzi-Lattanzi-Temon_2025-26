import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Sottomissione {
    id: string;
    teamIscrittoId: string;
    titolo: string;
    descrizione: string;
    linkRepository: string;
    valutata: boolean;
    voto: number | null;
    giudizio: string | null;
}

export interface CreaSottomissioneRequest {
    teamIscrittoId: string;
    hackathonId: string;
    titolo: string;
    descrizione: string;
    linkRepository: string;
}

export interface ValutaSottomissioneRequest {
    hackathonId: string;
    teamIscrittoId: string;
    voto: number;
    giudizio: string;
}

@Injectable({
    providedIn: 'root'
})
export class SottomissioneService {
    private readonly baseUrl = `${environment.apiUrl}/api/sottomissioni`;

    constructor(private http: HttpClient) {}

    getByHackathon(hackathonId: string): Observable<Sottomissione[]> {
        return this.http.get<Sottomissione[]>(`${this.baseUrl}/hackathon/${hackathonId}`);
    }

    carica(req: CreaSottomissioneRequest): Observable<Sottomissione> {
        return this.http.post<Sottomissione>(`${this.baseUrl}/carica`, req);
    }

    valuta(req: ValutaSottomissioneRequest): Observable<Sottomissione> {
        return this.http.post<Sottomissione>(`${this.baseUrl}/valuta`, req);
    }
}