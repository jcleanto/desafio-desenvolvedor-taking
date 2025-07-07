import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
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
    return this.http.delete<any>(url);
  }

}