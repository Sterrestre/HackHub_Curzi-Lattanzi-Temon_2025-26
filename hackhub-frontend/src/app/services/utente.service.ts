import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface UtenteDTO {
    id: string;
    nome: string;
    cognome: string;
    email: string;
    nickname: string;
    biografia: string;
    membroDiStaff: boolean;
    teamId: string | null;
    teamNome: string | null;
}

@Injectable({
    providedIn: 'root'
})
export class UtenteService {
    private readonly baseUrl = `${environment.apiUrl}/api/utenti`;

    constructor(private http: HttpClient) {}

    getCorrente(): Observable<UtenteDTO> {
        return this.http.get<UtenteDTO>(`${this.baseUrl}/me`);
    }

    logout(): Observable<any> {
        return this.http.post(`${this.baseUrl}/logout`, {});
    }
}