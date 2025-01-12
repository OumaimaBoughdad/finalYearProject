import { Component } from '@angular/core';
import { AuthService } from '../../auth.service'
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Importez FormsModule

@Component({
  selector: 'app-login',
  standalone: true, // Assurez-vous que le composant est autonome
  imports: [FormsModule], // Ajoutez FormsModule ici
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent {
  username: string = '';
  password: string = '';

  constructor(private authService: AuthService, private router: Router) {}

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
