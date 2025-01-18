import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Transaction } from '../models/transaction.model';
import jsPDF from 'jspdf';
import 'jspdf-autotable';
import {Compte} from '../models/compte.model'; // Importez jspdf-autotable pour les tables
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
  // Méthode pour exporter les transactions en PDF
  exportTransactionsToPDF(transactions: Transaction[], filename: string): void {
    const doc = new jsPDF();

    // Titre du PDF
    doc.setFontSize(18);
    doc.text('Liste des Transactions', 10, 10);

    // En-têtes de colonnes
    const headers = [
      ['ID', 'Type', 'Montant', 'Date', 'Comptes'],
    ];

    // Données des transactions
    const data = transactions.map((transaction) => [
      transaction.idTransaction,
      transaction.typeTransaction,
      transaction.montant,
      transaction.dateTransaction,
      this.formatComptes(transaction.comptes), // Formater les comptes
    ]);

    // Générer la table
    (doc as any).autoTable({
      head: headers,
      body: data,
      startY: 20, // Position de départ de la table
    });

    // Télécharger le PDF
    doc.save(`${filename}.pdf`);
  }

  // Méthode pour formater les comptes
  private formatComptes(comptes: Compte[]): string {
    return comptes.map((compte) => compte.numeroCompte).join(', ');
  }

  // Méthode pour formater l'employé
  private formatEmployee(employee: any): string {
    if (employee && employee.nom && employee.prenom) {
      return `${employee.nom} ${employee.prenom}`;
    }
    return 'N/A';
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
