// src/app/models/compte.model.ts
export interface CarteBancaire {
  idCarte: number;
  numeroCarte: string;
  dateExpiration: string;
  codePin: string;
  limiteCarte: number;

}

export interface Compte {
  idCompte: number;
  numeroCompte: string;
  typeCompte: 'COURANT' | 'EPARGNE';
  solde: number;
  dateOuverture: string;
  taux?: number; // Présent si le type est EPARGNE
  decouvert?: number; // Présent si le type est COURANT
  cartes: CarteBancaire[]; // Liste des cartes bancaires associées
  isEditing?: boolean; // Propriété optionnelle pour le mode édition

}
