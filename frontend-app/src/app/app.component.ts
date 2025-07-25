import { Component } from '@angular/core';
import { NavMenuComponent } from './components/nav-menu/nav-menu.component';
import { OverlayModule } from '@angular/cdk/overlay';
import { LoadingInterceptor } from './loading.interceptor';
import { HTTP_INTERCEPTORS } from '@angular/common/http';

@Component({
  selector: 'app-root',
  imports: [
    NavMenuComponent,
    OverlayModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoadingInterceptor,
      multi: true 
    },
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Desafio Desenvolvedor Taking';
}
