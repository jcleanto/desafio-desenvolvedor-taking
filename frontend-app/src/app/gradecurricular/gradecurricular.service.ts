import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CursoSemestreDisciplina } from './gradecurricular';

@Injectable({
  providedIn: 'root'
})
export class CursoSemestreDisciplinaService {

  constructor(private http: HttpClient) { }

  list(): Observable<CursoSemestreDisciplina[]> {
    const url = `${environment.apiUrl}/cursosemestredisciplina`;
    return this.http.get<CursoSemestreDisciplina[]>(url);
  }

  listByCurso(cursoId: number): Observable<CursoSemestreDisciplina[]> {
    const url = `${environment.apiUrl}/cursosemestredisciplina/${cursoId}`;
    return this.http.get<CursoSemestreDisciplina[]>(url);
  }

  create(cursoSemestreDisciplina: CursoSemestreDisciplina): Observable<CursoSemestreDisciplina> {
    const url = `${environment.apiUrl}/cursosemestredisciplina`;
    return this.http.post<CursoSemestreDisciplina>(url, cursoSemestreDisciplina);
  }

  delete(cursoSemestreDisciplina: CursoSemestreDisciplina): Observable<CursoSemestreDisciplina> {
    const url = `${environment.apiUrl}/cursosemestredisciplina/delete`;
    return this.http.post<CursoSemestreDisciplina>(url, cursoSemestreDisciplina);
  }

}