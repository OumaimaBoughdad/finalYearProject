import { Component } from '@angular/core';
import { AuthService } from '../../auth.service'

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  standalone: true
})
export class NavbarComponent {
  constructor(private authService: AuthService) {}

  onLogout() {
    this.authService.logout(); // Appelle la méthode de déconnexion
  }
}
