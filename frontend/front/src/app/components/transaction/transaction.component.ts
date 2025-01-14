import { Component, OnInit } from '@angular/core';
import { TransactionService } from '../../services/transaction.service';
import { AuthService } from '../../auth.service';
import { Transaction } from '../../models/transaction.model';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css'],
  standalone: true,
  imports :[CommonModule,FormsModule]
})
export class TransactionComponent implements OnInit {
  compteId: number = 0; // ID du compte source
  typeTransaction: 'DEBIT' | 'CREDIT' | 'TRANSFERT' = 'CREDIT'; // Type de transaction
  amount: number = 0; // Montant de la transaction
  targetCompteId: number | undefined; // ID du compte cible (pour les transferts)
  loggedInUser: string = ''; // Utilisateur connecté
  authToken: string = ''; // Token d'authentification

  transactions: Transaction[] = []; // Liste de toutes les transactions
  compteTransactions: Transaction[] = []; // Liste des transactions d'un compte spécifique
  selectedCompteId: number = 0; // ID du compte sélectionné pour afficher les transactions

  constructor(
    private transactionService: TransactionService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Récupérer l'utilisateur connecté et le token JWT
    this.loggedInUser = this.authService.employeeValue?.email || '';

    // Charger toutes les transactions au démarrage
    this.loadAllTransactions();
  }

  // Méthode pour effectuer une transaction
  makeTransaction(): void {
    if (!this.compteId || this.amount <= 0) {
      alert('Veuillez remplir tous les champs obligatoires.');
      return;
    }

    if (this.typeTransaction === 'TRANSFERT' && !this.targetCompteId) {
      alert('Veuillez spécifier un compte cible pour le transfert.');
      return;
    }

    this.transactionService
      .makeTransaction(
        this.compteId,
        this.typeTransaction,
        this.amount,
        this.targetCompteId,
        this.loggedInUser,
        this.authToken
      )
      .subscribe({
        next: (response) => {
          console.log('Transaction réussie :', response);
          alert('Transaction effectuée avec succès !');
          this.resetForm();
          this.loadAllTransactions(); // Recharger les transactions après une nouvelle transaction
        },
        error: (err) => {
          console.error('Erreur lors de la transaction :', err);
          alert('Erreur lors de la transaction. Veuillez réessayer.');
        },
      });
  }

  // Méthode pour charger toutes les transactions
  loadAllTransactions(): void {
    this.transactionService.getAllTransactions().subscribe({
      next: (transactions) => {
        this.transactions = transactions;
        console.log('Toutes les transactions :', transactions);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des transactions :', err);
      },
    });
  }

  // Méthode pour charger les transactions d'un compte spécifique
  loadTransactionsByCompteId(): void {
    if (this.selectedCompteId <= 0) {
      alert('Veuillez saisir un ID de compte valide.');
      return;
    }

    this.transactionService.getTransactionsByCompteId(this.selectedCompteId).subscribe({
      next: (transactions) => {
        this.compteTransactions = transactions;
        console.log(`Transactions du compte ${this.selectedCompteId} :`, transactions);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des transactions du compte :', err);
        alert('Aucune transaction trouvée pour ce compte.');
      },
    });
  }

  // Réinitialiser le formulaire
  resetForm(): void {
    this.compteId = 0;
    this.typeTransaction = 'CREDIT';
    this.amount = 0;
    this.targetCompteId = undefined;
  }
}
