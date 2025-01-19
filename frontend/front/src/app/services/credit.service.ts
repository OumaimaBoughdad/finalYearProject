import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Credit } from '../models/credit.model';

@Injectable({
  providedIn: 'root'
})
export class CreditService {
  private baseUrl = 'http://localhost:8091/api/credits';

  constructor(private http: HttpClient) {}

  // Récupérer tous les crédits
  getAllCredits(): Observable<Credit[]> {
    return this.http.get<Credit[]>(`${this.baseUrl}`);
  }

  // Récupérer un crédit par ID
  getCreditById(creditId: number): Observable<Credit> {
    return this.http.get<Credit>(`${this.baseUrl}/${creditId}`);
  }

  createCredit(cni: number, loanAmnt: number, loanIntent: string): Observable<Credit> {
    // Construire l'URL avec les query parameters
    const url = `${this.baseUrl}/api/create?cni=${cni}&loanIntent=${loanIntent}&loanAmnt=${loanAmnt}`;

    // Envoyer une requête POST sans corps (body)
    return this.http.post<Credit>(url, {});
  }

  // Mettre à jour un crédit
  updateCredit(creditId: number, updatedCredit: Credit): Observable<Credit> {
    return this.http.put<Credit>(`${this.baseUrl}/${creditId}`, updatedCredit);
  }

  deleteCredit(creditId: number): Observable<void> {
    const url = `${this.baseUrl}/deletecredit/${creditId}`; // Construire l'URL avec l'ID du crédit
    return this.http.delete<void>(url); // Envoyer une requête DELETE
  }
}
