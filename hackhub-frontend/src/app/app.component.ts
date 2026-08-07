import { Component } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { environment } from '../environments/environment';

@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet, RouterLink],
    templateUrl: './app.component.html'
})
export class AppComponent {
    title = 'HackHub';

    // URL del backend per il login: e' un semplice link, non una chiamata
    // HttpClient, perche' il login OAuth2 di Spring Security funziona
    // tramite redirect del browser, non tramite fetch/XHR.
    loginUrl = `${environment.apiUrl}/oauth2/authorization/google`;
}