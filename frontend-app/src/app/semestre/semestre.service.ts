import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Semestre } from './semestre';

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
    return this.http.post<Semestre>(url, semestre);
  }

  update(semestre: Semestre): Observable<Semestre> {
    const url = `${environment.apiUrl}/semestre/${semestre.id}`;
    return this.http.put<Semestre>(url, semestre);
  }

  delete(semestre: Semestre): Observable<any> {
    const url = `${environment.apiUrl}/semestre/${semestre.id}`;
    return this.http.delete<any>(url);
  }

}