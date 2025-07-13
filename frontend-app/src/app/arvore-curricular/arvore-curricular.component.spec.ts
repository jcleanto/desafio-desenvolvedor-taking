import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArvoreCurricularComponent } from './arvore-curricular.component';

describe('ArvoreCurricularComponent', () => {
  let component: ArvoreCurricularComponent;
  let fixture: ComponentFixture<ArvoreCurricularComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArvoreCurricularComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ArvoreCurricularComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
