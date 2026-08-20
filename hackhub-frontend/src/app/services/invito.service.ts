import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

export interface InvitaStaffRequest {
    hackathonId: string;
    email: string;
    ruolo: 'GIUDICE' | 'MENTORE';
}

export interface InvitoStaff {
    id: string;
    hackathonId: string;
    nomeHackathon: string;
    ruolo: string;
    mittenteNickname: string;
}

@Injectable({
    providedIn: 'root'
})
export class InvitoService {
    private readonly baseUrl = `${environment.apiUrl}/api/inviti`;

    constructor(private http: HttpClient) {}

    invitaStaff(req: InvitaStaffRequest): Observable<any> {
        return this.http.post(`${this.baseUrl}/staff`, req, { responseType: 'text' as 'json' });
    }

    getMieiInviti(): Observable<InvitoStaff[]> {
        return this.http.get<InvitoStaff[]>(`${this.baseUrl}/miei`);
    }

    rispondi(invitoId: string, accetta: boolean): Observable<any> {
        return this.http.post(`${this.baseUrl}/rispondi`, { invitoId, accetta }, { responseType: 'text' as 'json' });
    }
}