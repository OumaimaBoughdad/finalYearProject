import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PredictLoanComponent } from './predict-loan.component';

describe('PredictLoanComponent', () => {
  let component: PredictLoanComponent;
  let fixture: ComponentFixture<PredictLoanComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PredictLoanComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PredictLoanComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
