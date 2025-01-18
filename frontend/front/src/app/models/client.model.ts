// src/app/models/client.model.ts
export interface Client {
  idClient?: number; // Optionnel, car il est généré par le backend
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string; // Optionnel
  address: string;
  cne: string;
  isEditing?: boolean; // Propriété optionnelle pour le mode édition

  employee?: any; // Vous pouvez créer un modèle Employee si nécessaire
}
