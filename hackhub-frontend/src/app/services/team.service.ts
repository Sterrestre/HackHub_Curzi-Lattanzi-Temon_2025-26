import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CreaTeamRequest {
    nome: string;
}

export interface IscriviTeamRequest {
    teamId: string;
}

export interface IscrizioneTeam {
    teamIscrittoId: string;
    messaggio: string;
}

export interface AggiungiMembroRequest {
    teamId: string;
    utenteId: string;
    amministratore: boolean;
}

export interface MembroTeam {
    id: string;
    nickname: string;
    amministratore: boolean;
}

@Injectable({
    providedIn: 'root'
})
export class TeamService {
    private readonly teamUrl = `${environment.apiUrl}/api/team`;
    private readonly hackathonUrl = `${environment.apiUrl}/api/hackathon`;
    private readonly membriUrl = `${environment.apiUrl}/api/membri`;

    constructor(private http: HttpClient) {}

    crea(req: CreaTeamRequest): Observable<any> {
        return this.http.post(`${this.teamUrl}/crea`, req);
    }

    iscriviAHackathon(hackathonId: string, req: IscriviTeamRequest): Observable<IscrizioneTeam> {
        return this.http.post<IscrizioneTeam>(`${this.hackathonUrl}/${hackathonId}/iscrivi-team`, req);
    }

    getMembri(teamId: string): Observable<MembroTeam[]> {
        return this.http.get<MembroTeam[]>(`${this.membriUrl}/team/${teamId}`);
    }

    aggiungiMembro(req: AggiungiMembroRequest): Observable<MembroTeam> {
        return this.http.post<MembroTeam>(`${this.teamUrl}/aggiungi-membro`, req);
    }
}