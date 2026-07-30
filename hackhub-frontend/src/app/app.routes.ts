import { Routes } from '@angular/router';
import { HackathonListComponent } from './hackathon-list/hackathon-list.component';
import { HackathonDetailComponent } from './hackathon-detail/hackathon-detail.component';
import { HackathonFormComponent } from './hackathon-form/hackathon-form.component';
import { TeamEnrollComponent } from './team-enroll/team-enroll.component';

export const routes: Routes = [
    { path: '', component: HackathonListComponent },
    { path: 'nuovo-hackathon', component: HackathonFormComponent },
    { path: 'hackathon/:id', component: HackathonDetailComponent },
    { path: 'hackathon/:id/iscrivi', component: TeamEnrollComponent },
];