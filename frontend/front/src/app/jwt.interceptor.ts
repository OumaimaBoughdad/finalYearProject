import { HttpInterceptorFn } from '@angular/common/http';
import { AuthService } from './auth.service';
import { inject } from '@angular/core';

export const jwtInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService); // Injectez AuthService
  const token = authService.tokenValue;

  if (token) {
    // Clonez la requête et ajoutez l'en-tête d'autorisation
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    return next(authReq);
  }

  // Si aucun token n'est disponible, passez la requête originale
  return next(req);
};
