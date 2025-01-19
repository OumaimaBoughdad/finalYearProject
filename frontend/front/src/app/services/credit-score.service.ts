import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoanPredictionRequest, PredictLoanRequest } from '../models/loan-prediction.model';

@Injectable({
  providedIn: 'root'
})
export class CreditScoreService {
  private apiUrl = 'http://localhost:8080/api/credits';

  constructor(private http: HttpClient) {}

  predictLoanStatus(request: LoanPredictionRequest): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/predict`, request, { responseType: 'text' as 'json' });
  }


  // Méthode pour prédire le prêt pour un client existant
  predictLoanStatusByCNE(request: { cne: number, loanIntent: string, loanAmnt: number }): Observable<string> {
    return this.http.post<string>(`${this.apiUrl}/predictbycne`, request, { responseType: 'text' as 'json' });
  }
}
