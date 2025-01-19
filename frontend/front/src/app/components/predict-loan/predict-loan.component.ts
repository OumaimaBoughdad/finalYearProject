import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreditScoreService } from '../../services/credit-score.service';
import {CommonModule} from '@angular/common';
import {MaterialModules} from '../../shared/material';

@Component({
  selector: 'app-predict-loan',
  templateUrl: './predict-loan.component.html',
  standalone: true,
  imports:[CommonModule,MaterialModules],
  styleUrls: ['./predict-loan.component.css']
})
export class PredictLoanComponent {
  predictionForm: FormGroup;
  predictionResult: string | null = null;
  loading = false;
  existingClientForm: FormGroup;
  existingClientResult: string | null = null;  // Initialisation à null

  constructor(
    private fb: FormBuilder,
    private creditScoreService: CreditScoreService,
    private snackBar: MatSnackBar
  ) {
    this.predictionForm = this.fb.group({
      person_age: [null, Validators.required],
      person_income: [null, Validators.required],
      person_home_ownership: ['OWN', Validators.required],
      person_emp_length: [null, Validators.required],
      loan_intent: ['EDUCATION', Validators.required],
      loan_grade: ['B', Validators.required],
      loan_amnt: [null, [Validators.required, Validators.min(1000)]],
      loan_int_rate: [11.14, Validators.required],
      loan_percent_income: [0.10, Validators.required],
      cb_person_default_on_file: ['N', Validators.required],
      cb_person_cred_hist_length: [null, Validators.required]
    });
    // Formulaire pour le client existant
    this.existingClientForm = this.fb.group({
      cne: ['', Validators.required],
      loanIntent: ['', Validators.required],
      loanAmnt: ['', Validators.required],
    });
  }

  onSubmit() {
    if (this.predictionForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs obligatoires.', 'Fermer', { duration: 3000 });
      return;
    }

    const formData = this.predictionForm.value;
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
      }
    });

  }
  // Méthode de soumission pour le formulaire du client existant
  onSubmitExistingClient(): void {
    if (this.existingClientForm.valid) {
      this.loading = true;
      const formData = this.existingClientForm.value;

      // Remplacez ceci par l'appel API ou la logique de prédiction pour le client existant
      setTimeout(() => {
        this.existingClientResult = 'Résultat de la prédiction pour le client existant : ' + formData.loanAmnt;
        this.loading = false;
      }, 2000);
    }
  }
}
