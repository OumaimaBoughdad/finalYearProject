// Modèle pour une prédiction de prêt pour un nouveau client
export interface LoanPredictionRequest {
  person_age: number;
  person_income: number;
  person_home_ownership: string;
  person_emp_length: number;
  loan_intent: string;
  loan_amnt: number;
  cb_person_cred_hist_length: number;
}

// Modèle pour une prédiction de prêt pour un client existant
export interface PredictLoanRequest {
  cne: number;
  loanIntent: string;
  loanAmnt: number;
}

// Modèle pour la réponse de prédiction de prêt
export interface LoanPredictionResponse {
  prediction: string; // Le résultat de la prédiction (par exemple, "Approuvé" ou "Refusé")
  confidence?: number; // Optionnel, niveau de confiance de la prédiction
}
