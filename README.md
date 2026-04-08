import { ApplicationConfig, importProvidersFrom, isDevMode } from '@angular/core';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { ServiceWorkerModule } from '@angular/service-worker';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { routes } from './app.routes'; // Assuming app.routes.ts for routing
import { DEBUG_INFO_ENABLED } from './app.constants'; // Assuming app.constants.ts for debug info

// JHipster typically uses these interceptors
import { authInterceptorProviders } from './interceptors/auth.interceptor';
import { errorInterceptorProviders } from './interceptors/error.interceptor';
import { notificationInterceptorProviders } from './interceptors/notification.interceptor';
import { loadingInterceptorProviders } from './interceptors/loading.interceptor';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes, withComponentInputBinding()),
    provideHttpClient(withInterceptorsFromDi()),
    importProvidersFrom(
      BrowserModule,
      // Corrected: ServiceWorkerModule is enabled here as per README.md instruction
      // The README states: "To enable it, uncomment the following code in src/main/webapp/app/app.config.ts:
      // ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),"
      // To truly "enable" it, the 'enabled' flag should be set to true or dynamically based on environment.
      ServiceWorkerModule.register('ngsw-worker.js', {
        enabled: true, // Set to true to enable the service worker
        registrationStrategy: 'registerWhenStable:30000', // JHipster default strategy
      }),
      BrowserAnimationsModule, // Optional: Include if animations are used in the project
    ),
    // Application-specific providers
    authInterceptorProviders,
    errorInterceptorProviders,
    notificationInterceptorProviders,
    loadingInterceptorProviders,
    // Add other services, guards, etc. here
  ],
};