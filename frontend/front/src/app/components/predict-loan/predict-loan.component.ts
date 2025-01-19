import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreditScoreService } from '../../services/credit-score.service';
import { CommonModule } from '@angular/common';
import { MaterialModules } from '../../shared/material';

@Component({
  selector: 'app-predict-loan',
  templateUrl: './predict-loan.component.html',
  standalone: true,
  imports: [CommonModule, MaterialModules],
  styleUrls: ['./predict-loan.component.css'],
})
export class PredictLoanComponent {
  predictionForm: FormGroup;
  predictionResult: string | null = null;
  loading = false;
  existingClientForm: FormGroup;
  existingClientResult: string | null = null;

  constructor(
    private fb: FormBuilder,
    private creditScoreService: CreditScoreService,
    private snackBar: MatSnackBar
  ) {
    // Formulaire pour un nouveau client
    this.predictionForm = this.fb.group({
      person_age: [null, Validators.required],
      person_income: [null, Validators.required],
      person_home_ownership: ['OWN', Validators.required],
      person_emp_length: [null, Validators.required],
      loan_intent: ['EDUCATION', Validators.required],
      loan_amnt: [null, [Validators.required, Validators.min(1000)]],
      loan_int_rate: [11.14, Validators.required],
      loan_percent_income: [0.1, Validators.required],
    });

    // Formulaire pour le client existant
    this.existingClientForm = this.fb.group({
      cne: ['', Validators.required],
      loanIntent: ['', Validators.required],
      loanAmnt: ['', Validators.required],
    });
  }

  // Soumission du formulaire pour un nouveau client
  onSubmit() {
    if (this.predictionForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs obligatoires.', 'Fermer', { duration: 3000 });
      return;
    }

    // Ajouter des valeurs par défaut avant d'envoyer les données
    const formData = {
      ...this.predictionForm.value,
      cb_person_default_on_file: 'N', // Historique de défaut de paiement : Toujours "Non"
      cb_person_cred_hist_length: 0, // Longueur de l'historique : Toujours 0
      loan_grade: this.calculateLoanGrade(this.predictionForm.value.loan_amnt), // Calcul dynamique du grade
    };

    this.loading = true;

    this.creditScoreService.predictLoanStatus(formData).subscribe({
      next: (response: string) => {
        this.predictionResult = response;
        this.snackBar.open('Prédiction réussie !', 'Fermer', { duration: 3000 });
        this.loading = false;
      },
      error: (err) => {
        this.snackBar.open('Erreur lors de la prédiction.', 'Fermer', { duration: 3000 });
        console.error('Erreur API:', err);
        this.loading = false;
      },
    });
  }

  // Soumission du formulaire pour un client existant
  onSubmitExistingClient(): void {
    if (this.existingClientForm.valid) {
      this.loading = true;
      const formData = this.existingClientForm.value;

      this.creditScoreService.predictLoanStatusByCNE(formData).subscribe({
        next: (response: string) => {
          this.existingClientResult = response;
          this.snackBar.open('Prédiction réussie !', 'Fermer', { duration: 3000 });
          this.loading = false;
        },
        error: (err) => {
          this.snackBar.open('Erreur lors de la prédiction.', 'Fermer', { duration: 3000 });
          console.error('Erreur API:', err);
          this.loading = false;
        },
      });
    }
  }

  // Calcul dynamique du grade basé sur le montant du prêt
  private calculateLoanGrade(loanAmount: number): string {
    if (loanAmount < 1000) return 'A';
    if (loanAmount < 5000) return 'B';
    if (loanAmount < 10000) return 'C';
    return 'D';
  }
}
