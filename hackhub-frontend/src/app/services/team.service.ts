import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface CreaTeamRequest {
    nome: string;
    amministratoreId: string;
}

export interface IscriviTeamRequest {
    teamId: string;
    amministratoreId: string;
}

@Injectable({
    providedIn: 'root'
})
export class TeamService {
    private readonly teamUrl = `${environment.apiUrl}/team`;
    private readonly hackathonUrl = `${environment.apiUrl}/hackathon`;

    constructor(private http: HttpClient) {}

    crea(req: CreaTeamRequest): Observable<any> {
        return this.http.post(`${this.teamUrl}/crea`, req);
    }

    iscriviAHackathon(hackathonId: string, req: IscriviTeamRequest): Observable<any> {
        return this.http.post(`${this.hackathonUrl}/${hackathonId}/iscrivi-team`, req);
    }
}