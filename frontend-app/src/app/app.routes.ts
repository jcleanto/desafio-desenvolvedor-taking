import { Routes } from '@angular/router';
import { CursoComponent } from './curso/curso.component';
import { SemestreComponent } from './semestre/semestre.component';

export const routes: Routes = [
  { path: 'curso', component: CursoComponent },
  { path: 'semestre', component: SemestreComponent },
  { path: '', redirectTo: '/curso', pathMatch: 'full' },
];
