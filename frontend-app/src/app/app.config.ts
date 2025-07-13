import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { provideKeycloakAngular } from '../../keycloak.config';
import { includeBearerTokenInterceptor } from 'keycloak-angular';

export const appConfig: ApplicationConfig = {
  providers: [
    // keycloak
    provideKeycloakAngular(),
    provideHttpClient(withInterceptors([includeBearerTokenInterceptor])),

    provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withFetch())
  ]
};
