import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { environment } from '../../environments/environment';
import { Disciplina } from './disciplina';

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
    return this.http.post<Disciplina>(url, disciplina);
  }

  update(disciplina: Disciplina): Observable<Disciplina> {
    const url = `${environment.apiUrl}/disciplina/${disciplina.id}`;
    return this.http.put<Disciplina>(url, disciplina);
  }

  delete(disciplina: Disciplina): Observable<any> {
    const url = `${environment.apiUrl}/disciplina/${disciplina.id}`;
    return this.http.delete<any>(url)
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: HttpErrorResponse) {
    if (error.status === 0) {
      // A client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(`Backend returned code ${error.status}, body was: `, error.error);
    }
    // Return an observable with a user-facing error message.
    return throwError(() => new Error('Something bad happened; please try again later.'));
  }

}