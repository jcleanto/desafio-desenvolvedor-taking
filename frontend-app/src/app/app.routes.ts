import { Routes } from '@angular/router';
import { CursoComponent } from './curso/curso.component';

export const routes: Routes = [
  { path: 'curso', component: CursoComponent },
  { path: '', redirectTo: '/curso', pathMatch: 'full' },
];
