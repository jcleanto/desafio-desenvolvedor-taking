import { Routes } from '@angular/router';
import { CursoComponent } from './curso/curso.component';
import { SemestreComponent } from './semestre/semestre.component';
import { DisciplinaComponent } from './disciplina/disciplina.component';
import { GradecurricularComponent } from './gradecurricular/gradecurricular.component';
import { ArvoreCurricularComponent } from './arvore-curricular/arvore-curricular.component';
import { SideLoginComponent } from './components/side-login/side-login.component';
import { canActivateAuthRole } from './guards/auth-role.guard';
import { UserComponent } from './user/user.component';


export const routes: Routes = [
  { path: '', component: SideLoginComponent },
  {
    path: 'user',
    component: UserComponent,
    canActivate: [canActivateAuthRole],
    data: { role: 'admin' },
  },
  {
    path: 'curso',
    component: CursoComponent,
    canActivate: [canActivateAuthRole],
    data: { role: 'admin' },
  },
  {
    path: 'semestre',
    component: SemestreComponent,
    canActivate: [canActivateAuthRole],
    data: { role: 'admin' },
  },
  {
    path: 'disciplina',
    component: DisciplinaComponent,
    canActivate: [canActivateAuthRole],
    data: { role: 'admin' },
  },
  {
    path: 'gradecurricular',
    component: GradecurricularComponent,
    canActivate: [canActivateAuthRole],
    data: { role: 'admin' },
  },
  { path: 'arvorecurricular', component: ArvoreCurricularComponent },
  { path: '', redirectTo: '/curso', pathMatch: 'full' },
];
