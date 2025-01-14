import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../models/transaction.model';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {
  private apiUrl = 'http://localhost:8080/api/transaction'; // Remplacez par l'URL de votre API

  constructor(private http: HttpClient) {}

  // Méthode pour effectuer une transaction
  makeTransaction(
    compteId: number,
    typeTransaction: 'DEBIT' | 'CREDIT' | 'TRANSFERT',
    amount: number,
    targetCompteId?: number,
    loggedInUser?: string,
    authToken?: string
  ): Observable<any> {
    const headers = new HttpHeaders({
      loggedInUser: loggedInUser || '',
      Authorization: `Bearer ${authToken}`,
    });

    const body = {
      compteId,
      typeTransaction,
      amount,
      targetCompteId,
    };

    return this.http.post(`${this.apiUrl}/make`, body, { headers });
  }

  // Méthode pour récupérer toutes les transactions
  getAllTransactions(): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}`);
  }

  // Méthode pour récupérer les transactions d'un compte spécifique
  getTransactionsByCompteId(compteId: number): Observable<Transaction[]> {
    return this.http.get<Transaction[]>(`${this.apiUrl}/compte/${compteId}`);
  }
}
