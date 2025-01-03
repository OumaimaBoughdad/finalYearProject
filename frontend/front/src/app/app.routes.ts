import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EmployeComponent } from './components/employe/employe.component';
import { AddEmployeeComponent } from './components/add-employee/add-employee.component';
import { FormsModule } from '@angular/forms';

 export const routes: Routes = [
  { path: '', component: EmployeComponent },
  { path: 'employees', component: EmployeComponent },
  { path: 'add-employee', component: AddEmployeeComponent },  // Route pour le formulaire d'ajout
];

@NgModule({
  imports: [RouterModule.forRoot(routes), FormsModule],
  exports: [RouterModule],
})
export class AppRoutingModule {}
