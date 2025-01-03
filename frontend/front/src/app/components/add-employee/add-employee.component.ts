// add-employee.component.ts
import { Component } from '@angular/core';
import { Employee, EmployeeService } from '../../services/employe.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-add-employee',
  templateUrl: './add-employee.component.html',
  styleUrls: ['./add-employee.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule]
})
export class AddEmployeeComponent {
  employee = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    role: '',
    password: ''
  }; // Objet pour stocker les données du nouvel employé
  errorMessage: string = '';
  successMessage: string = '';


  constructor(private employeeService: EmployeeService) {}

  onSubmit() {
    this.employeeService.addEmployee(this.employee).subscribe(
      (response) => {
        console.log('Employee added', response);
      },
      (error) => {
        console.error('There was an error!', error);
      }
    );
  }
}
