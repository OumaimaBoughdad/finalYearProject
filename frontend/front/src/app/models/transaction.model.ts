import {Compte} from './compte.model';

export interface Transaction {
  idTransaction: number;
  montant: number;
  dateTransaction: string;
  typeTransaction: 'DEBIT' | 'CREDIT' | 'TRANSFERT';
  comptes: Compte[];
  employee: any; // Vous pouvez créer un modèle Employee si nécessaire
}
