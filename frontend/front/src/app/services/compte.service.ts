// src/app/services/compte.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Compte } from '../models/compte.model';

@Injectable({
  providedIn: 'root',
})
export class CompteService {
  private apiUrl = 'http://localhost:8080/api/compte';

  constructor(private http: HttpClient) {}

  getComptes(): Observable<Compte[]> {
    return this.http.get<Compte[]>(this.apiUrl);
  }

  getCompteByNumero(numeroCompte: string): Observable<Compte> {
    return this.http.get<Compte>(`${this.apiUrl}/${numeroCompte}`);
  }

  getComptesByClientCne(cne: string): Observable<Compte[]> {
    return this.http.get<Compte[]>(`${this.apiUrl}/cne/${cne}`);
  }

  createCompte(
    clientId: number,
    compte: Compte,
    loggedInUser: string
  ): Observable<Compte> {
    const headers = new HttpHeaders().set('loggedInUser', loggedInUser);
    return this.http.post<Compte>(`${this.apiUrl}/${clientId}`, compte, {
      headers,
    });
  }

  updateCompte(id: number, updatedCompte: Compte): Observable<Compte> {
    return this.http.put<Compte>(`${this.apiUrl}/${id}`, updatedCompte);
  }

  deleteCompte(idCompte: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${idCompte}`);
  }
}
