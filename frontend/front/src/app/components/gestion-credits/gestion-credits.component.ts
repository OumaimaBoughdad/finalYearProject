import { Component } from '@angular/core';
import { PredictLoanComponent } from '../predict-loan/predict-loan.component';
import { GrafanaPanelComponent } from '../grafana-panel/grafana-panel.component';
import { LoanStatusChartsComponent } from '../loan-status-charts/loan-status-charts.component';
import { ClientCreditComponent } from '../client-credit/client-credit.component';
import { CreditComponent } from '../credit/credit.component';
import {CommonModule} from '@angular/common';
import {MaterialModules} from '../../shared/material';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-gestion-credits',
  standalone: true,
  imports: [
    PredictLoanComponent,
    GrafanaPanelComponent,
    LoanStatusChartsComponent,
    ClientCreditComponent,
    CreditComponent,
    CommonModule,
    MaterialModules,
    FormsModule,
  ], // Importez tous les composants ici
  templateUrl: './gestion-credits.component.html',
  styleUrls: ['./gestion-credits.component.css']
})
export class GestionCreditsComponent {
  currentComponent: string | null = null; // Variable pour stocker le composant à afficher

  // Méthode pour afficher le composant correspondant
  showComponent(component: string) {
    this.currentComponent = component;
  }
}
