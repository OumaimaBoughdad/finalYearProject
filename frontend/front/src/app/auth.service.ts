import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private tokenSubject: BehaviorSubject<string | null>;
  public token: Observable<string | null>;

  private employeeSubject: BehaviorSubject<any | null>;
  public employee: Observable<any | null>;

  constructor(private http: HttpClient, private router: Router) {
    const initialToken = this.isLocalStorageAvailable()
      ? localStorage.getItem('token')
      : null;
    const initialEmployee = this.isLocalStorageAvailable()
      ? JSON.parse(localStorage.getItem('employee') || '{}')
      : null;

    this.tokenSubject = new BehaviorSubject<string | null>(initialToken);
    this.token = this.tokenSubject.asObservable();

    this.employeeSubject = new BehaviorSubject<any | null>(initialEmployee);
    this.employee = this.employeeSubject.asObservable();
  }

  public get tokenValue(): string | null {
    return this.tokenSubject.value;
  }

  public get employeeValue(): any | null {
    return this.employeeSubject.value;
  }

  login(username: string, password: string) {
    return this.http
      .post<any>(
        'http://localhost:8080/auth/token',
        { username, password },
        { responseType: 'text' as 'json' }
      )
      .pipe(
        map((response) => {
          console.log('Réponse du backend :', response); // Affiche le jeton (qui doit être une chaîne)
          if (response) {
            localStorage.setItem('token', response); // Stocke le token
            this.tokenSubject.next(response); // Met à jour le token

            // Récupérer les informations de l'employé après la connexion réussie
            this.http
              .get<any>(`http://localhost:8080/api/employees/${username}`)
              .subscribe({
                next: (employeeData) => {
                  localStorage.setItem('employee', JSON.stringify(employeeData)); // Stocke les infos de l'employé
                  this.employeeSubject.next(employeeData); // Met à jour les infos de l'employé
                },
                error: (err) => {
                  console.error('Erreur lors de la récupération des informations de l\'employé', err);
                },
              });
          } else {
            console.error('Token non reçu dans la réponse');
          }
          return response;
        })
      );
  }

  logout() {
    if (this.isLocalStorageAvailable()) {
      localStorage.removeItem('token');
      localStorage.removeItem('employee');
    }
    this.tokenSubject.next(null);
    this.employeeSubject.next(null);
    this.router.navigate(['/login']);
  }

  isAuthenticated(): boolean {
    return !!this.tokenValue;
  }

  private isLocalStorageAvailable(): boolean {
    try {
      const test = 'test';
      localStorage.setItem(test, test);
      localStorage.removeItem(test);
      return true;
    } catch (e) {
      return false;
    }
  }
}
