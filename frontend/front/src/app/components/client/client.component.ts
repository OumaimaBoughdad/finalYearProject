// src/app/components/client/client.component.ts
import { Component, OnInit } from '@angular/core';
import { ClientService } from '../../services/client.service';
import { Client } from '../../models/client.model';
import { AuthService } from '../../auth.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-client',
  templateUrl: './client.component.html',
  styleUrls: ['./client.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule], // Ajoutez FormsModule ici
})
export class ClientComponent implements OnInit {
  clients: Client[] = [];
  newClient: Client = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    address: '',
    cne: '',
  };
  loggedInUser: string = '';
  selectedClient: Client | null = null; // Pour stocker le client sélectionné pour la mise à jour
  searchCne: string = ''; // Pour la recherche par CNE

  constructor(
    private clientService: ClientService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loggedInUser = this.authService.employeeValue?.email || '';
    this.loadClients();
  }

  // Charger tous les clients
  loadClients(): void {
    this.clientService.getClients().subscribe({
      next: (clients) => (this.clients = clients),
      error: (err) => console.error('Failed to load clients', err),
    });
  }

  // Créer un nouveau client
  createClient(): void {
    this.clientService.createClient(this.newClient, this.loggedInUser).subscribe({
      next: (client) => {
        this.clients.push(client);
        this.newClient = {
          firstName: '',
          lastName: '',
          email: '',
          phoneNumber: '',
          address: '',
          cne: '',
        }; // Réinitialiser le formulaire
      },
      error: (err) => console.error('Failed to create client', err),
    });
  }

  // Supprimer un client
  deleteClient(id: number): void {
    this.clientService.deleteClient(id).subscribe({
      next: () => {
        this.clients = this.clients.filter((client) => client.idClient !== id);
      },
      error: (err) => console.error('Failed to delete client', err),
    });
  }

  // Récupérer un client par son ID
  getClientById(id: number): void {
    this.clientService.getClientById(id).subscribe({
      next: (client) => {
        this.selectedClient = client; // Stocker le client sélectionné
      },
      error: (err) => console.error('Failed to load client', err),
    });
  }

  // Mettre à jour un client
  updateClient(): void {
    if (this.selectedClient && this.selectedClient.idClient) {
      this.clientService.updateClient(this.selectedClient.idClient, this.selectedClient).subscribe({
        next: (updatedClient) => {
          // Mettre à jour la liste des clients
          const index = this.clients.findIndex((c) => c.idClient === updatedClient.idClient);
          if (index !== -1) {
            this.clients[index] = updatedClient;
          }
          this.selectedClient = null; // Réinitialiser le client sélectionné
        },
        error: (err) => console.error('Failed to update client', err),
      });
    }
  }

  // Récupérer un client par son CNE
  getClientByCne(): void {
    if (this.searchCne) {
      this.clientService.getClientByCne(this.searchCne).subscribe({
        next: (client) => {
          this.selectedClient = client; // Afficher les détails du client trouvé
        },
        error: (err) => console.error('Failed to load client by CNE', err),
      });
    }
  }
}
