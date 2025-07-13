import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { CursoSemestreDisciplina } from './gradecurricular';
import { handleError } from '../utils/handle.error';

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
    return this.http.post<CursoSemestreDisciplina>(url, cursoSemestreDisciplina)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar criar uma nova Grade Curricular. Verifique se a mesma já existe.'))
      );
  }

  delete(cursoSemestreDisciplina: CursoSemestreDisciplina): Observable<CursoSemestreDisciplina> {
    const url = `${environment.apiUrl}/cursosemestredisciplina/delete`;
    return this.http.post<CursoSemestreDisciplina>(url, cursoSemestreDisciplina)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar deletar a Grade Curricular. Por favor, tente novamente mais tarde.'))
      );
  }

}