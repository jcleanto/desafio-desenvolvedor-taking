import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Semestre } from './semestre';
import { handleError } from '../utils/handle.error';

@Injectable({
  providedIn: 'root'
})
export class SemestreService {

  constructor(private http: HttpClient) { }

  list(): Observable<Semestre[]> {
    const url = `${environment.apiUrl}/semestre`;
    return this.http.get<Semestre[]>(url);
  }

  create(semestre: Semestre): Observable<Semestre> {
    const url = `${environment.apiUrl}/semestre`;
    return this.http.post<Semestre>(url, semestre)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar criar um novo Semestre. Por favor, tente novamente mais tarde.'))
      );
  }

  update(semestre: Semestre): Observable<Semestre> {
    const url = `${environment.apiUrl}/semestre/${semestre.id}`;
    return this.http.put<Semestre>(url, semestre)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar atualizar o Semestre. Por favor, tente novamente mais tarde.'))
      );
  }

  delete(semestre: Semestre): Observable<any> {
    const url = `${environment.apiUrl}/semestre/${semestre.id}`;
    return this.http.delete<any>(url)
      .pipe(
        catchError(error => handleError(error, 'Delete primeiro as referências a esse Semestre na Grade Curricular.'))
      );
  }

}