// client-credit.component.ts
import { Component } from '@angular/core';
import { ClientCreditService, ClientCredit } from '../../client-credit.service';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {MaterialModules} from '../../shared/material';

@Component({
  selector: 'app-client-credit',
  templateUrl: './client-credit.component.html',
  standalone: true,
  imports:[CommonModule,FormsModule,MaterialModules],
  styleUrls: ['./client-credit.component.css']
})
export class ClientCreditComponent {

  // Variables pour ClientCredit
  cni: number = 123456;
  name: string = '';
  personAge: number = 30;
  personIncome: number = 25000;
  personHomeOwnership: string = '';
  personEmpLength: number = 5;
  cbPersonDefaultOnFile: string = '';
  cbPersonCredHistLength: number = 10;

  clientCredits: ClientCredit[] = [];
  clientDetails: ClientCredit | null = null;
  errorMessage: string | null = null;

  constructor(private clientCreditService: ClientCreditService) {}

  // Créer un ClientCredit
  createClientCredit() {
    const newClient: ClientCredit = {
      cni: this.cni,
      name: this.name,
      personAge: this.personAge,
      personIncome: this.personIncome,
      personHomeOwnership: this.personHomeOwnership,
      personEmpLength: this.personEmpLength,
      cbPersonDefaultOnFile: this.cbPersonDefaultOnFile,
      cbPersonCredHistLength: this.cbPersonCredHistLength
    };
    this.clientCreditService.createClientCredit(newClient).subscribe(
      (data) => {
        this.clientDetails = data;
        this.errorMessage = null; // Réinitialiser le message d'erreur
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la création du client de crédit.';
        this.clientDetails = null;
        console.error('Error creating client credit', error);
      }
    );
  }

  // Récupérer un ClientCredit par CNI
  getClientCredit() {
    this.clientCreditService.getClientCreditByCni(this.cni).subscribe(
      (data) => {
        this.clientDetails = data;
        this.errorMessage = null;
      },
      (error) => {
        this.errorMessage = 'Client de crédit non trouvé.';
        this.clientDetails = null;
        console.error('Error retrieving client credit', error);
      }
    );
  }

  // Supprimer un ClientCredit par CNI
  deleteClientCredit() {
    this.clientCreditService.deleteClientCredit(this.cni).subscribe(
      () => {
        this.errorMessage = null;
        this.clientDetails = null;
        this.getAllClientCredits();  // Actualiser la liste des clients de crédit
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la suppression du client de crédit.';
        console.error('Error deleting client credit', error);
      }
    );
  }

  // Récupérer tous les ClientCredits
  getAllClientCredits() {
    this.clientCreditService.getAllClientCredits().subscribe(
      (data) => {
        this.clientCredits = data;
        this.errorMessage = null;
      },
      (error) => {
        this.errorMessage = 'Erreur lors de la récupération des clients de crédit.';
        console.error('Error retrieving client credits', error);
      }
    );
  }
}
