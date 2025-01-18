import { Component } from '@angular/core';
import { AuthService } from '../../auth.service';
import { Router, RouterModule } from '@angular/router';
import { MaterialModules } from '../../shared/material';
import { ThemeService } from '../../services/theme.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  standalone: true,
  imports: [MaterialModules, RouterModule], // Utilisez les modules importés
})
export class NavbarComponent {
  constructor(
    private authService: AuthService,
    private router: Router,
    public themeService: ThemeService // Injectez le ThemeService
  ) {}

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }

  // Méthode pour basculer le thème
  toggleTheme(): void {
    this.themeService.toggleTheme();
  }
}
