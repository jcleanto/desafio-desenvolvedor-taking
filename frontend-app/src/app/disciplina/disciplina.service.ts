import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { Disciplina } from './disciplina';
import { handleError } from '../utils/handle.error';

@Injectable({
  providedIn: 'root'
})
export class DisciplinaService {

  constructor(private http: HttpClient) { }

  list(): Observable<Disciplina[]> {
    const url = `${environment.apiUrl}/disciplina`;
    return this.http.get<Disciplina[]>(url);
  }

  create(disciplina: Disciplina): Observable<Disciplina> {
    const url = `${environment.apiUrl}/disciplina`;
    return this.http.post<Disciplina>(url, disciplina)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar criar uma nova Disciplina. Tente novamente mais tarde.'))
      );
  }

  update(disciplina: Disciplina): Observable<Disciplina> {
    const url = `${environment.apiUrl}/disciplina/${disciplina.id}`;
    return this.http.put<Disciplina>(url, disciplina)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar atualizar a Disciplina. Tente novamente mais tarde.'))
      );
  }

  delete(disciplina: Disciplina): Observable<any> {
    const url = `${environment.apiUrl}/disciplina/${disciplina.id}`;
    return this.http.delete<any>(url)
      .pipe(
        catchError(error => handleError(error, 'Delete primeiro as referências a essa Disciplina na Grade Curricular.'))
      );
  }

}