import { Component, OnInit, ViewChild } from '@angular/core';
import { Employee, EmployeeService } from '../../services/employe.service';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MaterialModules } from '../../shared/material';
import { MatTableDataSource } from '@angular/material/table';
import { MatSort, Sort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import {AddEmployeeComponent} from '../add-employee/add-employee.component';

@Component({
  selector: 'app-employe',
  templateUrl: './employe.component.html',
  styleUrls: ['./employe.component.css'],
  standalone: true,
  imports: [MaterialModules, CommonModule, FormsModule, HttpClientModule, AddEmployeeComponent],
})
export class EmployeComponent implements OnInit {
  employees: Employee[] = [];
  dataSource: MatTableDataSource<Employee>;
  errorMessage: string = '';
  selectedEmployee: Employee | null = null;

  // Options de tri
  selectedSortOption: string = 'id'; // Par défaut, tri par ID
  sortOptions = [
    { value: 'id', label: 'ID' },
    { value: 'firstName', label: 'Prénom' },
    { value: 'lastName', label: 'Nom' },
    { value: 'email', label: 'Email' },
    { value: 'role', label: 'Rôle' },
  ];

  @ViewChild(MatSort) sort!: MatSort;
  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private employeeService: EmployeeService) {
    this.dataSource = new MatTableDataSource<Employee>([]);
  }

  ngOnInit(): void {
    this.fetchEmployees();
  }

  fetchEmployees(): void {
    this.employeeService.getAllEmployees().subscribe((data) => {
      this.employees = data;
      this.dataSource.data = this.employees; // Initialiser la source de données
      this.dataSource.sort = this.sort;
      this.dataSource.paginator = this.paginator;
    });
  }


  // Recherche universelle
  universalSearch(query: string): void {
    if (!query) {
      this.errorMessage = 'Veuillez saisir un ID ou un email.';
      this.dataSource.data = this.employees; // Réinitialiser les données
      return;
    }

    // Si la requête est un nombre, rechercher par ID
    if (!isNaN(Number(query))) {
      const employee = this.employees.find((emp) => emp.idEmployee === Number(query));
      if (employee) {
        this.dataSource.data = [employee]; // Afficher uniquement l'employé trouvé
        this.errorMessage = '';
      } else {
        this.errorMessage = 'Employé introuvable.';
        this.dataSource.data = []; // Aucun résultat
      }
    } else {
      // Sinon, rechercher par email
      const employee = this.employees.find((emp) => emp.email === query);
      if (employee) {
        this.dataSource.data = [employee]; // Afficher uniquement l'employé trouvé
        this.errorMessage = '';
      } else {
        this.errorMessage = 'Employé introuvable avec cet email.';
        this.dataSource.data = []; // Aucun résultat
      }
    }
  }

  // Réinitialiser la recherche
  resetSearch(): void {
    this.dataSource.data = this.employees; // Réinitialiser les données
    this.errorMessage = '';
  }

  // Appliquer le tri en fonction de l'option sélectionnée
  applySort(): void {
    const sortState: Sort = { active: this.selectedSortOption, direction: 'asc' };
    this.sort.active = sortState.active;
    this.sort.direction = sortState.direction;
    this.sort.sortChange.emit(sortState); // Déclencher le tri
  }

  deleteEmployee(id: number): void {
    this.employeeService.deleteEmployee(id).subscribe({
      next: () => {
        this.employees = this.employees.filter((emp) => emp.idEmployee !== id);
        this.dataSource.data = this.employees; // Mettre à jour la source de données
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors de la suppression de l\'employé.';
        console.error(err);
      },
    });
  }

  editEmployee(employee: Employee): void {
    employee.isEditing = true;
  }
  showAddEmployeeForm: boolean = false;

  toggleAddEmployeeForm(): void {
    this.showAddEmployeeForm = !this.showAddEmployeeForm; // Basculer l'affichage
  }

  saveEmployee(employee: Employee): void {
    this.employeeService.updateEmployee(employee.idEmployee, employee).subscribe({
      next: (data) => {
        const index = this.employees.findIndex((emp) => emp.idEmployee === employee.idEmployee);
        if (index !== -1) {
          this.employees[index] = data;
          this.dataSource.data = this.employees; // Mettre à jour la source de données
        }
        employee.isEditing = false;
        this.errorMessage = '';
      },
      error: (err) => {
        this.errorMessage = 'Erreur lors de la mise à jour de l\'employé.';
        console.error(err);
      },
    });
  }
}
