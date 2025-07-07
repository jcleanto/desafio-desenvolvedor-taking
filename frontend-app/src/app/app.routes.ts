import { Routes } from '@angular/router';
import { CursoComponent } from './curso/curso.component';
import { SemestreComponent } from './semestre/semestre.component';
import { DisciplinaComponent } from './disciplina/disciplina.component';

export const routes: Routes = [
  { path: 'curso', component: CursoComponent },
  { path: 'semestre', component: SemestreComponent },
  { path: 'disciplina', component: DisciplinaComponent },
  { path: '', redirectTo: '/curso', pathMatch: 'full' },
];
