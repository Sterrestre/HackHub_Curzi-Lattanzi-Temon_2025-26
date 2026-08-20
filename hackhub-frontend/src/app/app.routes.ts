import { Routes } from '@angular/router';
import { HackathonListComponent } from './hackathon-list/hackathon-list.component';
import { HackathonDetailComponent } from './hackathon-detail/hackathon-detail.component';
import { HackathonFormComponent } from './hackathon-form/hackathon-form.component';
import { TeamEnrollComponent } from './team-enroll/team-enroll.component';
import { TeamMembersComponent } from './team-members/team-members.component';
import { SottomissioneFormComponent } from './sottomissione-form/sottomissione-form.component';
import { MieiInvitiComponent } from './miei-inviti/miei-inviti.component';
import { TeamCreateComponent } from './team-create/team-create.component';
import { ProfiloComponent } from './profilo/profilo.component';

export const routes: Routes = [
    { path: '', component: HackathonListComponent },
    { path: 'nuovo-hackathon', component: HackathonFormComponent },
    { path: 'hackathon/:id', component: HackathonDetailComponent },
    { path: 'hackathon/:id/iscrivi', component: TeamEnrollComponent },
    { path: 'hackathon/:id/sottometti', component: SottomissioneFormComponent },
    { path: 'team/:teamId', component: TeamMembersComponent },
    { path: 'miei-inviti', component: MieiInvitiComponent },
    { path: 'crea-team', component: TeamCreateComponent },
    { path: 'profilo', component: ProfiloComponent },
];