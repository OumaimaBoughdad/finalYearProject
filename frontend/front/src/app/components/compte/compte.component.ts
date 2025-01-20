import { Component, OnInit } from '@angular/core';
import { CompteService } from '../../services/compte.service';
import {CarteBancaire, Compte} from '../../models/compte.model';
import { AuthService } from '../../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Client } from '../../models/client.model';
import {ClientService} from '../../services/client.service'; // Importez le modèle Client
import { Router } from '@angular/router';

@Component({
  selector: 'app-compte',
  templateUrl: './compte.component.html',
  styleUrls: ['./compte.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule],
})
export class CompteComponent implements OnInit {
  comptes: Compte[] = []; // Liste des comptes
  newCompte: Compte = {
    idCompte: 0,
    numeroCompte: '',
    typeCompte: 'COURANT',
    solde: 0,
    dateOuverture: new Date().toISOString().split('T')[0],
    taux: 0,
    decouvert: 0,
    cartes: [],
  }; // Nouveau compte à créer
  selectedCompte: Compte | null = null; // Compte sélectionné pour la modification
  searchNumeroCompte: string = ''; // Pour la recherche par numéro de compte
  searchClientCne: string = ''; // Pour la recherche par CNE du client
  loggedInUser: string = ''; // Utilisateur connecté (récupéré dynamiquement)
  selectedCompteCartes: CarteBancaire[] | null = null; // Pour stocker les cartes du compte sélectionné
  clients: Client[] = []; // Liste des clients
  selectedClientId: number | null = null; // ID du client sélectionné
  constructor(
    private router: Router,
    private compteService: CompteService,
    private clientService: ClientService, // Injectez ClientService

    private authService: AuthService // Injectez AuthService
  ) {}

  ngOnInit(): void {
    this.loadComptes();
    this.loadClients(); // Ajouter ici

    this.loggedInUser = this.authService.employeeValue?.email || ''; // Récupérez l'utilisateur connecté
  }

  // Méthode pour naviguer vers le tableau de bord des comptes
  goToCompteDashboard() {
    this.router.navigate(['/comptedashboard']);
  }

  // Charger tous les comptes
  loadComptes(): void {
    this.compteService.getComptes().subscribe({
      next: (comptes) => (this.comptes = comptes),
      error: (err) => console.error('Failed to load comptes', err),
    });
  }
  // Charger tous les clients
  loadClients(): void {
    this.clientService.getClients().subscribe({
      next: (clients) => (this.clients = clients),
      error: (err) => console.error('Failed to load clients', err),
    });
  }
  // Afficher les cartes d'un compte
  showCartes(compte: Compte): void {
    this.selectedCompteCartes = compte.cartes; // Stocker les cartes du compte sélectionné
  }

  // Fermer l'affichage des cartes
  closeCartes(): void {
    this.selectedCompteCartes = null;
  }

  // Rechercher un compte par son numéro
  searchByNumero(): void {
    if (this.searchNumeroCompte) {
      this.compteService.getCompteByNumero(this.searchNumeroCompte).subscribe({
        next: (compte) => {
          this.comptes = [compte]; // Afficher uniquement le compte trouvé
        },
        error: (err) => console.error('Failed to load compte by numero', err),
      });
    }
  }

  // Rechercher les comptes d'un client par son CNE
  searchByClientCne(): void {
    if (this.searchClientCne) {
      this.compteService.getComptesByClientCne(this.searchClientCne).subscribe({
        next: (comptes) => (this.comptes = comptes),
        error: (err) => console.error('Failed to load comptes by client CNE', err),
      });
    }
  }

  // Créer un nouveau compte
  createCompte(): void {
    if (this.selectedClientId) {
      this.compteService.createCompte(this.selectedClientId, this.newCompte, this.loggedInUser).subscribe({
        next: (compte) => {
          this.comptes.push(compte); // Ajouter le nouveau compte à la liste
          this.resetNewCompteForm(); // Réinitialiser le formulaire
        },
        error: (err) => console.error('Failed to create compte', err),
      });
    } else {
      console.error('No client selected');
    }
  }

  // Sélectionner un compte pour la modification
  selectCompteForUpdate(compte: Compte): void {
    this.selectedCompte = { ...compte }; // Crée une copie du compte
  }

  // Mettre à jour un compte
  updateCompte(): void {
    if (this.selectedCompte && this.selectedCompte.idCompte) {
      this.compteService.updateCompte(this.selectedCompte.idCompte, this.selectedCompte).subscribe({
        next: (updatedCompte) => {
          const index = this.comptes.findIndex((c) => c.idCompte === updatedCompte.idCompte);
          if (index !== -1) {
            this.comptes[index] = updatedCompte; // Mettre à jour la liste
          }
          this.selectedCompte = null; // Réinitialiser le compte sélectionné
        },
        error: (err) => console.error('Failed to update compte', err),
      });
    }
  }

  // Supprimer un compte
  deleteCompte(idCompte: number): void {
    this.compteService.deleteCompte(idCompte).subscribe({
      next: () => {
        this.comptes = this.comptes.filter((c) => c.idCompte !== idCompte); // Retirer le compte de la liste
      },
      error: (err) => console.error('Failed to delete compte', err),
    });
  }

  // Réinitialiser le formulaire de création de compte
  resetNewCompteForm(): void {
    this.newCompte = {
      idCompte: 0,
      numeroCompte: '',
      typeCompte: 'COURANT',
      solde: 0,
      dateOuverture: new Date().toISOString().split('T')[0],
      taux: 0,
      decouvert: 0,
      cartes: [],
    };
  }
}
