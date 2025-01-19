export interface Credit {
  creditId?: number; // Optionnel, car il peut être généré par le backend
  cni: number; // Identifiant du client associé au crédit
  loanAmnt: number; // Montant du prêt
  loanIntent: string; // Intention du prêt (par exemple, "EDUCATION", "PERSONAL", etc.)
  loanStatus?: string; // Optionnel, statut du prêt (peut être renvoyé par le backend)
  createdAt?: Date; // Optionnel, date de création du crédit
  updatedAt?: Date; // Optionnel, date de mise à jour du crédit
}
