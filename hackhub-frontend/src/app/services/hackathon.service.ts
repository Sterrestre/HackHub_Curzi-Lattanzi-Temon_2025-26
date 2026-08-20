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
    organizzatoreId: string;
}

export interface CreaHackathonRequest {
    nome: string;
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

export interface StaffHackathon {
    organizzatoreNickname: string;
    giudiceNickname: string | null;
    mentoriNickname: string[];
}

@Injectable({
    providedIn: 'root'
})
export class HackathonService {
    private readonly baseUrl = `${environment.apiUrl}/api/hackathon`;

    constructor(private http: HttpClient) {}

    getAll(): Observable<Hackathon[]> {
        return this.http.get<Hackathon[]>(`${this.baseUrl}/all`);
    }

    getById(id: string): Observable<Hackathon> {
        return this.http.get<Hackathon>(`${this.baseUrl}/${id}`);
    }

    getClassifica(id: string): Observable<string[]> {
        return this.http.get<string[]>(`${this.baseUrl}/${id}/classifica`);
    }

    crea(req: CreaHackathonRequest): Observable<any> {
        return this.http.post(`${this.baseUrl}/crea`, req);
    }

    conferma(id: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/${id}/conferma`, {}, { responseType: 'text' as 'json' });
    }

    confermaClassifica(id: string): Observable<any> {
        return this.http.post(`${this.baseUrl}/${id}/conferma-classifica`, {}, { responseType: 'text' as 'json' });
    }

    sonoStaff(id: string): Observable<boolean> {
        return this.http.get<boolean>(`${this.baseUrl}/${id}/sono-staff`);
    }

    getStaff(id: string): Observable<StaffHackathon> {
        return this.http.get<StaffHackathon>(`${this.baseUrl}/${id}/staff`);
    }

    getMieiComeTeam(): Observable<Hackathon[]> {
        return this.http.get<Hackathon[]>(`${this.baseUrl}/miei-team`);
    }

    getMieiComeStaff(): Observable<Hackathon[]> {
        return this.http.get<Hackathon[]>(`${this.baseUrl}/miei-staff`);
    }
}