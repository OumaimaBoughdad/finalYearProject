// client-credit.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ClientCredit {
  cni: number;
  name: string;
  personAge: number;
  personIncome: number;
  personHomeOwnership: string;
  personEmpLength: number;
  cbPersonDefaultOnFile: string;
  cbPersonCredHistLength: number;
}

@Injectable({
  providedIn: 'root'
})
export class ClientCreditService {

  private baseUrl = 'http://localhost:8080/api/credits';  // URL de votre backend

  constructor(private http: HttpClient) {}

  // Créer un ClientCredit
  createClientCredit(client: ClientCredit): Observable<ClientCredit> {
    return this.http.post<ClientCredit>(`${this.baseUrl}/create-client`, client);
  }

  // Récupérer un ClientCredit par CNI
  getClientCreditByCni(cni: number): Observable<ClientCredit> {
    return this.http.get<ClientCredit>(`${this.baseUrl}/getclient/${cni}`);
  }

  // Récupérer tous les ClientCredits
  getAllClientCredits(): Observable<ClientCredit[]> {
    return this.http.get<ClientCredit[]>(`${this.baseUrl}/clients`);
  }

  // Supprimer un ClientCredit par CNI
  deleteClientCredit(cni: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/deleteclient/${cni}`);
  }
}
