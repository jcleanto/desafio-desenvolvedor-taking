import { Injectable } from '@angular/core';
import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { ComponentPortal } from '@angular/cdk/portal';
import { LoaderComponent } from './loader-component/loader.component';

@Injectable({
  providedIn: 'root',
})
export class LoaderService {
  private overlayRef: OverlayRef | null = null;

  constructor(private overlay: Overlay) {}

  show() {
    if (!this.overlayRef) {
      this.overlayRef = this.overlay.create({
        hasBackdrop: true, // Add a backdrop to prevent interaction
        backdropClass: 'cdk-overlay-dark-backdrop',
        positionStrategy: this.overlay.position().global().centerHorizontally().centerVertically(),
      });
      this.overlayRef.attach(new ComponentPortal(LoaderComponent));
    }
  }

  hide() {
    if (this.overlayRef) {
      this.overlayRef.detach();
      this.overlayRef = null;
    }
  }
}