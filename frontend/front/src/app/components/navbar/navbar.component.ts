// src/app/navbar/navbar.component.ts
import { Component } from '@angular/core';
import { AuthService } from '../../auth.service';
import { Router, RouterModule } from '@angular/router';
import { MaterialModules } from '../../shared/material';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
  standalone: true,
  imports: [MaterialModules, RouterModule], // Utilisez les modules importés
})
export class NavbarComponent {
  constructor(private authService: AuthService, private router: Router) {}

  onLogout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
