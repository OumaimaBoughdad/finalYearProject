import { Component, OnInit } from '@angular/core';
import { Employee, EmployeeService } from '../../services/employe.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-employe',
  templateUrl: './employe.component.html',
  styleUrls: ['./employe.component.css'],
  standalone: true,
  imports: [CommonModule,FormsModule, HttpClientModule]
})
export class EmployeComponent implements OnInit {
  employees: Employee[] = [];
  searchResult?: Employee;
  errorMessage: string = ''; // Déclaration de la propriété errorMessage
  selectedEmployee: Employee | null = null; // Pour stocker l'employé sélectionné à modifier

  constructor(private employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.fetchEmployees();
  }

  fetchEmployees(): void {
    this.employeeService.getAllEmployees().subscribe((data) => {
      this.employees = data;
    });
  }

  deleteEmployee(id: number): void {
    this.employeeService.deleteEmployee(id).subscribe({
      next: () => {
        this.employees = this.employees.filter((emp) => emp.idEmployee !== id);
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors de la suppression de l\'employé.';
        console.error(err);
      },
    });
  }

  searchById(id: number): void {
    this.employeeService.getEmployeeById(id).subscribe({
      next: (data) => {
        this.searchResult = data;
        this.errorMessage = '';
      },
      error: (err) => {
        this.searchResult = undefined;
        this.errorMessage = 'Employé introuvable.';
        console.error(err);
      },
    });
  }

  searchByEmail(email: string): void {
    this.employeeService.getEmployeeByEmail(email).subscribe({
      next: (data) => {
        this.searchResult = data;
        this.errorMessage = '';
      },
      error: (err) => {
        this.searchResult = undefined;
        this.errorMessage = 'Employé introuvable avec cet email.';
        console.error(err);
      },
    });
  }

  // Nouvelle fonction pour activer l'édition de l'employé
  editEmployee(employee: Employee): void {
    employee.isEditing = true;
  }

  // Nouvelle fonction pour sauvegarder l'employé après modification
  saveEmployee(employee: Employee): void {
    this.employeeService.updateEmployee(employee.idEmployee, employee).subscribe({
      next: (data) => {
        // Mettre à jour l'employé dans la liste après la modification
        const index = this.employees.findIndex((emp) => emp.idEmployee === employee.idEmployee);
        if (index !== -1) {
          this.employees[index] = data;
        }
        employee.isEditing = false; // Désactiver le mode édition
        this.errorMessage = ''; // Réinitialiser le message d'erreur
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors de la mise à jour de l\'employé.';
        console.error(err);
      },
    });
  }
}
