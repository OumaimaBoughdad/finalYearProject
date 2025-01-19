import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, Validators} from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CreditService } from '../../services/credit.service';
import { Credit } from '../../models/credit.model';
import {CommonModule} from '@angular/common';
import {MaterialModules} from '../../shared/material';

@Component({
  selector: 'app-credit',
  templateUrl: './credit.component.html',
  standalone: true,
  imports:[CommonModule,FormsModule,MaterialModules],
  styleUrls: ['./credit.component.css']
})
export class CreditComponent implements OnInit {
  credits: Credit[] = []; // Liste des crédits
  creditForm: FormGroup; // Formulaire pour créer/mettre à jour un crédit
  isEditing = false; // Mode édition (true) ou création (false)
  currentCreditId: null = null; // ID du crédit en cours d'édition

  constructor(
    private fb: FormBuilder,
    private creditService: CreditService,
    private snackBar: MatSnackBar
  ) {
    // Initialisation du formulaire
    this.creditForm = this.fb.group({
      cni: [null, [Validators.required, Validators.min(1)]], // CNI du client
      loanAmnt: [null, [Validators.required, Validators.min(1000)]], // Montant du prêt
      loanIntent: ['PERSONAL', Validators.required] // Intention du prêt
    });
  }

  ngOnInit(): void {
    this.loadCredits(); // Charger la liste des crédits au démarrage
  }

  // Charger la liste des crédits
  loadCredits(): void {
    this.creditService.getAllCredits().subscribe({
      next: (credits) => {
        this.credits = credits;
      },
      error: (err) => {
        this.snackBar.open('Erreur lors du chargement des crédits.', 'Fermer', { duration: 3000 });
        console.error('Erreur API:', err);
      }
    });
  }

  onSubmit(): void {
    if (this.creditForm.invalid) {
      this.snackBar.open('Veuillez remplir tous les champs correctement.', 'Fermer', { duration: 3000 });
      return;
    }

    const formData = this.creditForm.value;
    this.creditService.createCredit(formData.cni, formData.loanAmnt, formData.loanIntent).subscribe({
      next: (newCredit) => {
        this.snackBar.open('Crédit créé avec succès.', 'Fermer', { duration: 3000 });
        this.credits.push(newCredit); // Ajouter le nouveau crédit à la liste
        this.resetForm(); // Réinitialiser le formulaire
      },
      error: (err) => {
        this.snackBar.open('Erreur lors de la création du crédit.', 'Fermer', { duration: 3000 });
        console.error('Erreur API:', err);
      }
    });
  }
  // Supprimer un crédit
  deleteCredit(creditId: number): void {
    if (confirm('Êtes-vous sûr de vouloir supprimer ce crédit ?')) {
      this.creditService.deleteCredit(creditId).subscribe({
        next: () => {
          this.snackBar.open('Crédit supprimé avec succès.', 'Fermer', { duration: 3000 });
          this.credits = this.credits.filter(credit => credit.creditId !== creditId); // Retirer le crédit de la liste
        },
        error: (err) => {
          this.snackBar.open('Erreur lors de la suppression du crédit.', 'Fermer', { duration: 3000 });
          console.error('Erreur API:', err);
        }
      });
    }
  }

  // Réinitialiser le formulaire
  resetForm(): void {
    this.creditForm.reset();
    this.isEditing = false;
    this.currentCreditId = null;
  }
}
