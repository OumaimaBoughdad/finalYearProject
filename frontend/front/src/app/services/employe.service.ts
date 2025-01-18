import { Injectable } from '@angular/core';
import { HttpClient,HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Employee {
  idEmployee: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  role: string;
  isEditing?: boolean; // Cette propriété peut être optionnelle

}

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  private apiUrl = 'http://localhost:8080/api/employees';

  constructor(private http: HttpClient) {}

  getAllEmployees(): Observable<Employee[]> {
    return this.http.get<Employee[]>(this.apiUrl);
  }
  // Récupérer un employé par ID
  getEmployeeById(id: number): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/${id}`);
  }

  // Mettre à jour un employé existant
  updateEmployee(id: number, employee: Partial<Employee>): Observable<Employee> {
    return this.http.put<Employee>(`${this.apiUrl}/${id}`, employee);
  }

  deleteEmployee(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // // Récupérer un employé par email
  // getEmployeeByEmail(email: string): Observable<Employee> {
  //   return this.http.get<Employee>(`${this.apiUrl}/by-email?email=${email}`);
  // }

  // Récupérer un employé par email
  getEmployeesByLastName(lastname: string): Observable<Employee> {
    return this.http.get<Employee>(`${this.apiUrl}/by-lastname?lastName=${lastname}`);
  }

  addEmployee(employee: any) {
    const headers = new HttpHeaders().set('Content-Type', 'application/json');
    return this.http.post('http://localhost:8080/api/employees', employee, { headers: headers });
  }
}
