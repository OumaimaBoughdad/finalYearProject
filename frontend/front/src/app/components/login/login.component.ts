import { Component } from '@angular/core';
import { AuthService } from '../../auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';
import {CommonModule} from '@angular/common';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    FormsModule,
    CommonModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatFormFieldModule,
  ],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  isActive: boolean = false; // Initialement, le formulaire est caché
  showSignInButton: boolean = true; // Contrôle la visibilité du bouton Sign In
  showPassword: boolean = false; // Pour basculer l'affichage du mot de passe


  username: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

  toggleActive(): void {
    this.isActive = true; // Activer le formulaire Sign In
    this.showSignInButton = false; // Cache le bouton Sign In

  }
  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword; // Bascule entre true et false
  }

  onSubmit() {
    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/']);
      },
      error: (err) => {
        console.error('Erreur de connexion', err);
      },
    });
  }
}
