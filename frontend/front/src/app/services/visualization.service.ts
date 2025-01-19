import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';

// Interfaces pour les DTO renvoyés par l'API
export interface LoanStatusByDefaultDTO {
  defaultOnFile: string;
  noDefaultCount: number; // Correspond à "noDefaultCount" dans l'API
  defaultCount: number;   // Correspond à "defaultCount" dans l'API
}

export interface LoanStatusByCreditHistoryDTO {
  creditHistoryLength: string;
  noDefaultCount: number; // Correspond à "noDefaultCount" dans l'API
  defaultCount: number;   // Correspond à "defaultCount" dans l'API
}

@Injectable({
  providedIn: 'root' // Ce service est disponible dans toute l'application
})
export class VisualizationService {
  // URL de base de l'API backend
  private apiUrl = 'http://localhost:8080/api/credits';

  constructor(private http: HttpClient) {}

  /**
   * Récupère les données pour le graphique "Loan Status by Default on File".
   * @returns Un observable contenant un tableau de LoanStatusByDefaultDTO.
   */
  getLoanStatusByDefaultOnFile(): Observable<LoanStatusByDefaultDTO[]> {
    return this.http.get<LoanStatusByDefaultDTO[]>(`${this.apiUrl}/loan-status-by-default`).pipe(
      catchError(this.handleError) // Gestion des erreurs
    );
  }

  /**
   * Récupère les données pour le graphique "Loan Status by Credit History Length".
   * @returns Un observable contenant un tableau de LoanStatusByCreditHistoryDTO.
   */
  getLoanStatusByCreditHistoryLength(): Observable<LoanStatusByCreditHistoryDTO[]> {
    return this.http.get<LoanStatusByCreditHistoryDTO[]>(`${this.apiUrl}/loan-status-by-credit-history`).pipe(
      catchError(this.handleError) // Gestion des erreurs
    );
  }

  /**
   * Gère les erreurs HTTP.
   * @param error L'erreur renvoyée par HttpClient.
   * @returns Un observable avec un message d'erreur.
   */
  private handleError(error: HttpErrorResponse): Observable<never> {
    console.error('An error occurred:', error);
    return throwError(() => new Error('Something went wrong; please try again later.'));
  }
}
