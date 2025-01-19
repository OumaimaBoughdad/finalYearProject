import { Component, OnInit, ViewChild } from '@angular/core';
import { ClientService } from '../../services/client.service';
import { Client } from '../../models/client.model';
import { AuthService } from '../../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MaterialModules } from '../../shared/material';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort, Sort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.css'],
  standalone: true,
  imports: [MaterialModules, FormsModule, CommonModule],
})
export class ClientComponent implements OnInit {
  clients: Client[] = []; // Liste complète des clients
  dataSource: MatTableDataSource<Client>; // Source de données pour le tableau
  errorMessage: string = ''; // Message d'erreur
  loggedInUser: string = ''; // Utilisateur connecté
  searchQuery: string = ''; // Pour la recherche universelle (ID ou CNE)
  showAddForm: boolean = false; // Pour contrôler l'affichage du formulaire d'ajout
  newClient: Client = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    address: '',
    cne: '',
  }; // Nouveau client à ajouter

  // Options de tri
  selectedSortOption: string = 'id'; // Par défaut, tri par ID
  sortOptions = [
    { value: 'id', label: 'ID' },
    { value: 'firstName', label: 'Prénom' },
    { value: 'lastName', label: 'Nom' },
    { value: 'email', label: 'Email' },
    { value: 'cne', label: 'CNE' },
  ];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(
    private clientService: ClientService,
    private authService: AuthService
  ) {
    this.dataSource = new MatTableDataSource<Client>([]); // Initialiser la source de données
  }

  ngOnInit(): void {
    this.loggedInUser = this.authService.employeeValue?.email || '';
    this.loadClients(); // Charger tous les clients au démarrage
  }

  // Charger tous les clients
  loadClients(): void {
    this.clientService.getClients().subscribe({
      next: (clients) => {
        this.clients = clients;
        this.dataSource.data = clients; // Initialiser la source de données
        this.dataSource.sort = this.sort;
        this.dataSource.paginator = this.paginator;
      },
      error: (err) => console.error('Failed to load clients', err),
    });
  }

  // Recherche universelle
  universalSearch(query: string): void {
    if (!query) {
      this.errorMessage = 'Veuillez saisir un ID ou un CNE.';
      this.dataSource.data = this.clients; // Réinitialiser les données
      return;
    }

    // Si la requête est un nombre, rechercher par ID
    if (!isNaN(Number(query))) {
      const client = this.clients.find((c) => c.idClient === Number(query));
      if (client) {
        this.dataSource.data = [client]; // Afficher uniquement le client trouvé
        this.errorMessage = '';
      } else {
        this.errorMessage = 'Client introuvable.';
        this.dataSource.data = []; // Aucun résultat
      }
    } else {
      // Sinon, rechercher par CNE
      const client = this.clients.find((c) => c.cne === query);
      if (client) {
        this.dataSource.data = [client]; // Afficher uniquement le client trouvé
        this.errorMessage = '';
      } else {
        this.errorMessage = 'Client introuvable avec ce CNE.';
        this.dataSource.data = []; // Aucun résultat
      }
    }
  }

  // Réinitialiser la recherche
  resetSearch(): void {
    this.dataSource.data = this.clients; // Réinitialiser les données
    this.errorMessage = '';
  }

  // Appliquer le tri en fonction de l'option sélectionnée
  applySort(): void {
    const sortState: Sort = { active: this.selectedSortOption, direction: 'asc' };
    this.sort.active = sortState.active;
    this.sort.direction = sortState.direction;
    this.sort.sortChange.emit(sortState); // Déclencher le tri
  }

  // Supprimer un client
  deleteClient(id: number): void {
    this.clientService.deleteClient(id).subscribe({
      next: () => {
        this.clients = this.clients.filter((c) => c.idClient !== id);
        this.dataSource.data = this.clients; // Mettre à jour la source de données
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors de la suppression du client.';
        console.error(err);
      },
    });
  }

  // Activer le mode édition pour un client
  editClient(client: Client): void {
    client.isEditing = true; // Activer le mode édition
  }

  // Sauvegarder les modifications
  saveClient(client: Client): void {
    if (client.idClient) {
      this.clientService.updateClient(client.idClient, client).subscribe({
        next: (updatedClient) => {
          const index = this.clients.findIndex((c) => c.idClient === updatedClient.idClient);
          if (index !== -1) {
            this.clients[index] = updatedClient; // Mettre à jour le client dans la liste
          }
          this.dataSource.data = this.clients; // Mettre à jour la source de données
          client.isEditing = false; // Désactiver le mode édition
          this.errorMessage = '';
        },
        error: (err) => {
          this.errorMessage = 'Erreur lors de la mise à jour du client.';
          console.error(err);
        },
      });
    }
  }

  // Ouvrir le formulaire d'ajout
  openAddClientForm(): void {
    this.showAddForm = true;
  }

  // Créer un nouveau client
  createClient(): void {
    this.clientService.createClient(this.newClient, this.loggedInUser).subscribe({
      next: (client) => {
        this.clients.push(client); // Ajouter le nouveau client à la liste
        this.dataSource.data = this.clients; // Mettre à jour la source de données
        this.newClient = {
          firstName: '',
          lastName: '',
          email: '',
          phoneNumber: '',
          address: '',
          cne: '',
        }; // Réinitialiser le formulaire
        this.showAddForm = false; // Masquer le formulaire après l'ajout
      },
      error: (err) => console.error('Failed to create client', err),
    });
  }

  // Annuler l'ajout
  cancelAdd(): void {
    this.showAddForm = false; // Masquer le formulaire
    this.newClient = {
      firstName: '',
      lastName: '',
      email: '',
      phoneNumber: '',
      address: '',
      cne: '',
    }; // Réinitialiser le formulaire
  }
}
