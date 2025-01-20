import { Component, EventEmitter, Output } from '@angular/core'; // Import correct
import { EmployeeService } from '../../services/employe.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  selector: 'app-add-employee',
  templateUrl: './add-employee.component.html',
  styleUrls: ['./add-employee.component.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, MatButtonModule, MatInputModule, MatFormFieldModule],
  providers: [EmployeeService] // Fournir le service ici
})
export class AddEmployeeComponent {
  employee = {
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    role: '',
    password: ''
  };
  errorMessage: string = '';
  successMessage: string = '';
  @Output() cancel = new EventEmitter<void>(); // Déclaration correcte de l'événement

  constructor(private employeeService: EmployeeService) {}

  onSubmit() {
    this.employeeService.addEmployee(this.employee).subscribe(
      (response) => {
        console.log('Employee added', response);
        this.successMessage = 'Employé ajouté avec succès !';
        this.errorMessage = '';
        setTimeout(() => {
          this.cancel.emit(); // Masquer le formulaire après l'ajout
        }, 0);
      },
      (error) => {
        console.error('There was an error!', error);
        this.errorMessage = 'Erreur lors de l\'ajout de l\'employé.';
        this.successMessage = '';
      }
    );
  }

  onCancel(): void {
    this.cancel.emit(); // Masquer le formulaire
  }
}
