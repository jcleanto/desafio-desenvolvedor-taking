import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { handleError } from '../utils/handle.error';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  list(): Observable<User[]> {
    const url = `${environment.apiUrl}/user`;
    return this.http.get<User[]>(url);
  }

  create(user: User): Observable<User> {
    const url = `${environment.apiUrl}/user`;
    return this.http.post<User>(url, user)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar criar um novo Usuário. Por favor, tente novamente mais tarde.'))
      );
  }

  update(user: User): Observable<User> {
    const url = `${environment.apiUrl}/user/${user.id}`;
    return this.http.put<User>(url, user)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar atualizar o Usuário. Por favor, tente novamente mais tarde.'))
      );;
  }

  delete(user: User): Observable<any> {
    const url = `${environment.apiUrl}/user/${user.id}`;
    return this.http.delete<any>(url)
      .pipe(
        catchError(error => handleError(error, 'Erro ao tentar deletar esse Usuário. Por favor, tente novamente mais tarde.'))
      );
  }
}
