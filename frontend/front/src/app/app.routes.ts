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

export const routes: Routes = [
  { path: '', component: HomeComponent , canActivate: [AuthGuard]},
  { path: 'employees', component: EmployeComponent},
  { path: 'add-employee', component: AddEmployeeComponent, canActivate: [AuthGuard] },  // Route pour le formulaire d'ajout
   { path: 'login', component: LoginComponent },
   { path: 'clients', component: ClientComponent, canActivate: [AuthGuard] },
  { path: 'comptes', component: CompteComponent, canActivate: [AuthGuard]},
  { path: 'transaction', component: TransactionComponent, canActivate: [AuthGuard] },
  { path: 'dashboard', component: DashboardComponent},
  { path: 'comptedashboard', component: CompteDashboardComponent },

 ];

@NgModule({
  imports: [RouterModule.forRoot(routes), FormsModule],
  exports: [RouterModule],
})
export class AppRoutingModule {}
