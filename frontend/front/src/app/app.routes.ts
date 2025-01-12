import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeComponent } from './components/employe/employe.component';
import { AddEmployeeComponent } from './components/add-employee/add-employee.component';
import { FormsModule } from '@angular/forms';
import { AuthGuard } from './auth.guard';
import { LoginComponent } from './components/login/login.component';
import {ClientComponent} from './components/client/client.component';

export const routes: Routes = [
  { path: '', component: EmployeComponent , canActivate: [AuthGuard]},
  { path: 'employees', component: EmployeComponent,canActivate: [AuthGuard] },
  { path: 'add-employee', component: AddEmployeeComponent,canActivate: [AuthGuard] },  // Route pour le formulaire d'ajout
   { path: 'login', component: LoginComponent },
   { path: 'clients', component: ClientComponent,canActivate: [AuthGuard] },


 ];

@NgModule({
  imports: [RouterModule.forRoot(routes), FormsModule],
  exports: [RouterModule],
})
export class AppRoutingModule {}
