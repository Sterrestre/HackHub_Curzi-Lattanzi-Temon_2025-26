import { Routes } from '@angular/router';
import { HackathonListComponent } from './hackathon-list/hackathon-list.component';

export const routes: Routes = [
    { path: '', component: HackathonListComponent },
    // Prossime rotte da aggiungere, ad esempio:
    // { path: 'login', component: LoginComponent },
    // { path: 'hackathon/:id', component: HackathonDetailComponent },
];