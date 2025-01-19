import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoanStatusChartsComponent } from './loan-status-charts.component';

describe('LoanStatusChartsComponent', () => {
  let component: LoanStatusChartsComponent;
  let fixture: ComponentFixture<LoanStatusChartsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LoanStatusChartsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoanStatusChartsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
