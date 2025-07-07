import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Curso } from './curso';

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
    return this.http.post<Curso>(url, curso);
  }

  update(curso: Curso): Observable<Curso> {
    const url = `${environment.apiUrl}/curso/${curso.id}`;
    return this.http.put<Curso>(url, curso);
  }

  delete(curso: Curso): Observable<any> {
    const url = `${environment.apiUrl}/curso/${curso.id}`;
    return this.http.delete<any>(url);
  }


}
