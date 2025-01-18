import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort } from '@angular/material/sort';
import {MatPaginator, MatPaginatorModule} from '@angular/material/paginator';
import { TransactionService } from '../../services/transaction.service';
import { AuthService } from '../../auth.service';
import { Transaction } from '../../models/transaction.model';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatIconModule } from '@angular/material/icon';
import {MaterialModules} from '../../shared/material';
import { MatTableModule } from '@angular/material/table';
import {MatSortModule} from '@angular/material/sort';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css'],
  standalone: true,
  imports: [
    MatSortModule,
    MatTableModule,
    MatPaginatorModule,
    CommonModule,
    FormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatIconModule,
  ],
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

  // Propriétés pour la table Material (toutes les transactions)
  displayedColumns: string[] = [
    'idTransaction',
    'montant',
    'dateTransaction',
    'typeTransaction',
    'comptes',
    'employee',
  ];
  dataSourceAll = new MatTableDataSource<Transaction>(); // Source de données pour toutes les transactions
  @ViewChild('allTransactionsSort') allTransactionsSort!: MatSort;
  @ViewChild('allTransactionsPaginator') allTransactionsPaginator!: MatPaginator;

  // Propriétés pour la table Material (transactions du compte)
  dataSourceCompte = new MatTableDataSource<Transaction>(); // Source de données pour les transactions du compte
  @ViewChild('compteTransactionsSort') compteTransactionsSort!: MatSort;
  @ViewChild('compteTransactionsPaginator') compteTransactionsPaginator!: MatPaginator;
  // Propriété pour gérer la section active
  currentSection: 'form' | 'allTransactions' | 'compteTransactions' = 'form';
  constructor(
    private transactionService: TransactionService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    // Récupérer l'utilisateur connecté et le token JWT
    this.loggedInUser = this.authService.employeeValue?.email || '';
    this.authToken = this.authService.employeeValue?.token || '';

    // Charger toutes les transactions au démarrage
    this.loadAllTransactions();
  }

  ngAfterViewInit(): void {
    // Activer le tri et la pagination pour toutes les transactions
    this.dataSourceAll.sort = this.allTransactionsSort;
    this.dataSourceAll.paginator = this.allTransactionsPaginator;

    // Activer le tri et la pagination pour les transactions du compte
    this.dataSourceCompte.sort = this.compteTransactionsSort;
    this.dataSourceCompte.paginator = this.compteTransactionsPaginator;
  }

  // Méthode pour basculer entre les sections
  showSection(section: 'form' | 'allTransactions' | 'compteTransactions'): void {
    this.currentSection = section;
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
        this.dataSourceAll.data = transactions; // Mettre à jour la source de données pour toutes les transactions
        console.log('Toutes les transactions :', transactions);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des transactions :', err);
      },
    });
  }

  // Méthode pour exporter les transactions en PDF
  exportToPDF(): void {
    this.transactionService.exportTransactionsToPDF(this.transactions, 'transactions');
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
        this.dataSourceCompte.data = transactions; // Mettre à jour la source de données pour les transactions du compte
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
