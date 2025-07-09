import { Routes } from '@angular/router';
import { CursoComponent } from './curso/curso.component';
import { SemestreComponent } from './semestre/semestre.component';
import { DisciplinaComponent } from './disciplina/disciplina.component';
import { GradecurricularComponent } from './gradecurricular/gradecurricular.component';

export const routes: Routes = [
  { path: 'curso', component: CursoComponent },
  { path: 'semestre', component: SemestreComponent },
  { path: 'disciplina', component: DisciplinaComponent },
  { path: 'gradecurricular', component: GradecurricularComponent },
  { path: '', redirectTo: '/curso', pathMatch: 'full' },
];
