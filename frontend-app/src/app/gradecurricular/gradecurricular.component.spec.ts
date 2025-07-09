import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GradecurricularComponent } from './gradecurricular.component';

describe('GradecurricularComponent', () => {
  let component: GradecurricularComponent;
  let fixture: ComponentFixture<GradecurricularComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GradecurricularComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GradecurricularComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
