// /*import { Component, OnInit } from '@angular/core';
// import { CommonModule } from '@angular/common';
// import { FormsModule } from '@angular/forms';
// import { clientcredit } from '../../services/clientcredit.service'; // Import du service
// import { CreditClient as ClientModel } from '../../models/credit-client.model'; // Alias pour l'interface
//
// @Component({
//   selector: 'app-client',
//   templateUrl: './client.component.html',
//   standalone: true,
//   imports: [CommonModule, FormsModule], // Importez les modules nécessaires
//   styleUrls: ['./client.component.css']
// })
// export class CreditClient implements OnInit { // Gardez le nom CreditClient
//   clients: ClientModel[] = []; // Utilisez l'alias ClientModel
//   newClient: ClientModel = { // Utilisez l'alias ClientModel
//     cni: 0,
//     name: '',
//     personAge: 0,
//     personIncome: 0,
//     personHomeOwnership: '',
//     personEmpLength: 0,
//     cbPersonDefaultOnFile: '',
//     cbPersonCredHistLength: 0
//   }; // Nouveau client à créer
//
//   constructor(private clientService: clientcredit) {} // Injection du service
//
//   ngOnInit(): void {
//     this.loadClients(); // Charge les clients au démarrage
//   }
//
//   /**
//    * Charge tous les clients depuis l'API.
//    */
//   loadClients(): void {
//     this.clientService.getAllClients().subscribe({
//       next: (data) => {
//         this.clients = data;
//       },
//       error: (err) => {
//         console.error('Failed to load clients:', err);
//       }
//     });
//   }
//
//   /**
//    * Crée un nouveau client.
//    */
//   createClient(): void {
//     this.clientService.saveClient(this.newClient).subscribe({
//       next: (data) => {
//         console.log('Client created:', data);
//         this.loadClients(); // Recharge la liste des clients après création
//         this.resetForm(); // Réinitialise le formulaire
//       },
//       error: (err) => {
//         console.error('Failed to create client:', err);
//       }
//     });
//   }
// /*
//   /**
//    * Supprime un client par son CNI.
//    * @param cni Le CNI du client à supprimer.
//    *//*
//   deleteClient(cni: number): void {
//     this.clientService.deleteClient(cni).subscribe({
//       next: () => {
//         console.log('Client deleted');
//         this.loadClients(); // Recharge la liste des clients après suppression
//       },
//       error: (err) => {
//         console.error('Failed to delete client:', err);
//       }
//     });
//   }
//
//   /**
//    * Réinitialise le formulaire de création de client.
//    */
//   /*resetForm(): void {
//     this.newClient = {
//       cni: 0,
//       name: '',
//       personAge: 0,
//       personIncome: 0,
//       personHomeOwnership: '',
//       personEmpLength: 0,
//       cbPersonDefaultOnFile: '',
//       cbPersonCredHistLength: 0
//     };
//   }
// }*/
