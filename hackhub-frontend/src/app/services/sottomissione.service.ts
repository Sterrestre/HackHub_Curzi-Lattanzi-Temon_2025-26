import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface Sottomissione {
    id: string;
    titolo: string;
    descrizione: string;
    linkRepository: string;
    valutata: boolean;
    voto: number | null;
    giudizio: string | null;
}

@Injectable({
    providedIn: 'root'
})
export class SottomissioneService {
    private readonly baseUrl = `${environment.apiUrl}/sottomissioni`;

    constructor(private http: HttpClient) {}

    getByHackathon(hackathonId: string): Observable<Sottomissione[]> {
        return this.http.get<Sottomissione[]>(`${this.baseUrl}/hackathon/${hackathonId}`);
    }
}