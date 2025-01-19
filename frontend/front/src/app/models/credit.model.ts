export interface Credit {
  creditId?: number; // Optionnel, généré par le backend
  cni: number; // Identifiant du client
  loanAmnt: number; // Montant du prêt
  loanIntent: string; // Intention du prêt (ex. EDUCATION, PERSONAL, etc.)
  loanStatus?: string; // Statut du prêt
  createdAt?: Date; // Date de création
  updatedAt?: Date; // Date de mise à jour
}
