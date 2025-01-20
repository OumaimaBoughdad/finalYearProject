import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeComponent } from './components/employe/employe.component';
import { AddEmployeeComponent } from './components/add-employee/add-employee.component';
import { FormsModule } from '@angular/forms';
import { AuthGuard } from './auth.guard';
import { LoginComponent } from './components/login/login.component';
import {ClientComponent} from './components/client/client.component';
import {CompteComponent} from './components/compte/compte.component';
import {TransactionComponent} from './components/transaction/transaction.component';
import {HomeComponent}from './components/home/home.component';
import {DashboardComponent} from './Dashboards/dashboard/dashboard.component';
import {CompteDashboardComponent} from './Dashboards/compte-dashboard/compte-dashboard.component';
import {PredictLoanComponent} from './components/predict-loan/predict-loan.component';
import {LoanStatusChartsComponent} from './components/loan-status-charts/loan-status-charts.component';
import {GrafanaPanelComponent} from './components/grafana-panel/grafana-panel.component';
import {CreditComponent} from './components/credit/credit.component';
import {ClientCreditComponent} from './components/client-credit/client-credit.component';
import {GestionCreditsComponent} from './components/gestion-credits/gestion-credits.component';

export const routes: Routes = [
  { path: '', component: HomeComponent , canActivate: [AuthGuard]},
  { path: 'employees', component: EmployeComponent},
  { path: 'add-employee', component: AddEmployeeComponent, canActivate: [AuthGuard] },  // Route pour le formulaire d'ajout
   { path: 'login', component: LoginComponent },
   { path: 'clients', component: ClientComponent, canActivate: [AuthGuard] },
  { path: 'comptes', component: CompteComponent, canActivate: [AuthGuard]},
  { path: 'transaction', component: TransactionComponent, canActivate: [AuthGuard] },
  { path: 'dashboard', component: DashboardComponent, canActivate: [AuthGuard]},
  { path: 'comptedashboard', component: CompteDashboardComponent, canActivate: [AuthGuard] },
  { path: 'predict', component: PredictLoanComponent, canActivate: [AuthGuard] }, // Route pour PredictLoanComponent
  {path:'chart',component:LoanStatusChartsComponent, canActivate: [AuthGuard]},
  {path:'grafana',component:GrafanaPanelComponent, canActivate: [AuthGuard]},
  {path:'credits',component:CreditComponent, canActivate: [AuthGuard]},
  { path: 'gestion-credits', component: GestionCreditsComponent, canActivate: [AuthGuard] }, // Route pour la gestion des crédits
 ];

@NgModule({
  imports: [RouterModule.forRoot(routes), FormsModule],
  exports: [RouterModule],
})
export class AppRoutingModule {}
