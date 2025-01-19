import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Credit } from '../models/credit.model';

@Injectable({
  providedIn: 'root'
})
export class CreditService {
  private apiUrl = 'http://localhost:8091/api/credits';

  constructor(private http: HttpClient) {}

  createCredit(cni: number, loanAmnt: number, loanIntent: string): Observable<Credit> {
    return this.http.post<Credit>(this.apiUrl, null, {
      params: {
        cni: cni.toString(),
        loanAmnt: loanAmnt.toString(),
        loanIntent: loanIntent
      }
    });
  }

  getAllCredits(): Observable<Credit[]> {
    return this.http.get<Credit[]>(this.apiUrl);
  }

  getCreditById(creditId: number): Observable<Credit> {
    return this.http.get<Credit>(`${this.apiUrl}/${creditId}`);
  }

  updateCredit(creditId: number, updatedCredit: Credit): Observable<Credit> {
    return this.http.put<Credit>(`${this.apiUrl}/${creditId}`, updatedCredit);
  }

  deleteCredit(creditId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${creditId}`);
  }
}
