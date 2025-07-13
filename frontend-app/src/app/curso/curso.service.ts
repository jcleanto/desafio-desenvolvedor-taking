import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Curso } from './curso';
import { handleError } from '../utils/handle.error';

@Injectable({
  providedIn: 'root'
})
export class CursoService {

  constructor(private http: HttpClient) { }

  list(): Observable<Curso[]> {
    const url = `${environment.apiUrl}/curso`;
    return this.http.get<Curso[]>(url);
  }

  create(curso: Curso): Observable<Curso> {
    const url = `${environment.apiUrl}/curso`;
    return this.http.post<Curso>(url, curso)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar criar um novo Curso. Tente novamente mais tarde.'))
      );
  }

  update(curso: Curso): Observable<Curso> {
    const url = `${environment.apiUrl}/curso/${curso.id}`;
    return this.http.put<Curso>(url, curso)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar atualizar o Curso. Tente novamente mais tarde.'))
      );;
  }

  delete(curso: Curso): Observable<any> {
    const url = `${environment.apiUrl}/curso/${curso.id}`;
    return this.http.delete<any>(url)
      .pipe(
        catchError(error => handleError(error, 'Delete primeiro as referências a esse Curso na Grade Curricular.'))
      );
  }


}
